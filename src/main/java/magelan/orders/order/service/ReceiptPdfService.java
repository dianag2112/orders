package magelan.orders.order.service;

import lombok.RequiredArgsConstructor;
import magelan.orders.order.model.Order;
import magelan.orders.order.model.OrderItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptPdfService {

    private static final float MM_TO_POINTS = 72f / 25.4f;

    // Standard thermal receipt width: 80 mm
    private static final float PAGE_WIDTH = 80f * MM_TO_POINTS;

    private static final float LEFT_MARGIN = 6f;
    private static final float RIGHT_MARGIN = 6f;
    private static final float TOP_MARGIN = 6f;
    private static final float BOTTOM_MARGIN = 6f;

    private static final float CONTENT_WIDTH =
            PAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;

    private static final float MINIMUM_PAGE_HEIGHT = 180f;

    private static final String FONT_PATH =
            "fonts/NotoSans-Regular.ttf";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final OrderService orderService;

    public byte[] generateReceipt(UUID orderId, String requestedLanguage) {
        Order order = orderService.getCompletedOrderById(orderId);

        String language = normalizeLanguage(requestedLanguage);

        try (
                PDDocument document = new PDDocument();
                InputStream fontStream = new ClassPathResource(FONT_PATH)
                        .getInputStream();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {
            PDType0Font font = PDType0Font.load(
                    document,
                    fontStream,
                    true
            );

            List<ReceiptCommand> commands =
                    buildReceiptCommands(order, language, font);

            float pageHeight = calculatePageHeight(commands);

            PDPage page = new PDPage(
                    new PDRectangle(PAGE_WIDTH, pageHeight)
            );

            document.addPage(page);

            try (
                    PDPageContentStream contentStream =
                            new PDPageContentStream(document, page)
            ) {
                contentStream.setNonStrokingColor(0);
                contentStream.setStrokingColor(0);
                contentStream.setLineWidth(0.5f);

                renderCommands(
                        contentStream,
                        font,
                        commands,
                        pageHeight
                );
            }

            document.save(outputStream);

            return outputStream.toByteArray();

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "The receipt PDF could not be generated.",
                    exception
            );
        }
    }

    private List<ReceiptCommand> buildReceiptCommands(
            Order order,
            String language,
            PDFont font
    ) throws IOException {

        List<ReceiptCommand> commands = new ArrayList<>();

        addText(
                commands,
                "MAGELAN",
                20f,
                Alignment.CENTER
        );

        addSpacer(commands, 1f);
        addSeparator(commands);

        addWrappedText(
                commands,
                translate(language, "Поръчка: ", "Order: ")
                        + plainText(order.getOrderName()),
                12f,
                Alignment.LEFT,
                font
        );

        addWrappedText(
                commands,
                translate(language, "Създадена: ", "Created: ")
                        + DATE_FORMATTER.format(order.getCreatedOn()),
                10.5f,
                Alignment.LEFT,
                font
        );

        addSeparator(commands);

        for (OrderItem item : order.getItems()) {

            String productName = localizedProductName(
                    item.getProduct().getName(),
                    language
            );

            addWrappedText(
                    commands,
                    item.getQuantity() + " x " + productName,
                    13f,
                    Alignment.LEFT,
                    font
            );

            addRow(
                    commands,
                    item.getQuantity()
                            + " x "
                            + formatMoney(item.getUnitPrice()),
                    formatMoney(item.getTotalPrice()),
                    11.5f
            );

            addSpacer(commands, 1.5f);
        }

        addSeparator(commands);

        addRow(
                commands,
                translate(language, "ОБЩО", "TOTAL"),
                formatMoney(order.getAmount()),
                17f
        );

        addSeparator(commands);
        addSpacer(commands, 1f);

        addText(
                commands,
                translate(language, "Благодарим Ви!", "Thank you!"),
                13.5f,
                Alignment.CENTER
        );

        return commands;
    }

    private void renderCommands(
            PDPageContentStream contentStream,
            PDFont font,
            List<ReceiptCommand> commands,
            float pageHeight
    ) throws IOException {

        float cursorY = pageHeight - TOP_MARGIN;

        for (ReceiptCommand command : commands) {
            switch (command.type()) {
                case TEXT -> {
                    float baseline = cursorY - command.fontSize();

                    drawText(
                            contentStream,
                            font,
                            command.leftText(),
                            command.fontSize(),
                            command.alignment(),
                            baseline
                    );

                    cursorY -= command.advance();
                }

                case ROW -> {
                    float baseline = cursorY - command.fontSize();

                    drawTextAt(
                            contentStream,
                            font,
                            command.leftText(),
                            command.fontSize(),
                            LEFT_MARGIN,
                            baseline
                    );

                    float rightTextWidth = textWidth(
                            font,
                            command.rightText(),
                            command.fontSize()
                    );

                    float rightX =
                            PAGE_WIDTH
                                    - RIGHT_MARGIN
                                    - rightTextWidth;

                    drawTextAt(
                            contentStream,
                            font,
                            command.rightText(),
                            command.fontSize(),
                            rightX,
                            baseline
                    );

                    cursorY -= command.advance();
                }

                case SEPARATOR -> {
                    cursorY -= 3f;

                    contentStream.moveTo(
                            LEFT_MARGIN,
                            cursorY
                    );

                    contentStream.lineTo(
                            PAGE_WIDTH - RIGHT_MARGIN,
                            cursorY
                    );

                    contentStream.stroke();

                    cursorY -= command.advance() - 3f;
                }

                case SPACER ->
                        cursorY -= command.advance();
            }
        }
    }

    private void drawText(
            PDPageContentStream contentStream,
            PDFont font,
            String text,
            float fontSize,
            Alignment alignment,
            float y
    ) throws IOException {

        float width = textWidth(font, text, fontSize);

        float x = switch (alignment) {
            case LEFT -> LEFT_MARGIN;

            case CENTER ->
                    Math.max(
                            LEFT_MARGIN,
                            (PAGE_WIDTH - width) / 2f
                    );

            case RIGHT ->
                    Math.max(
                            LEFT_MARGIN,
                            PAGE_WIDTH - RIGHT_MARGIN - width
                    );
        };

        drawTextAt(
                contentStream,
                font,
                text,
                fontSize,
                x,
                y
        );
    }

    private void drawTextAt(
            PDPageContentStream contentStream,
            PDFont font,
            String text,
            float fontSize,
            float x,
            float y
    ) throws IOException {

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private float calculatePageHeight(
            List<ReceiptCommand> commands
    ) {
        float contentHeight = commands.stream()
                .map(ReceiptCommand::advance)
                .reduce(0f, Float::sum);

        return Math.max(
                MINIMUM_PAGE_HEIGHT,
                TOP_MARGIN + contentHeight + BOTTOM_MARGIN
        );
    }

    private void addWrappedText(
            List<ReceiptCommand> commands,
            String text,
            float fontSize,
            Alignment alignment,
            PDFont font
    ) throws IOException {

        List<String> wrappedLines = wrapText(
                text,
                font,
                fontSize,
                CONTENT_WIDTH
        );

        for (String line : wrappedLines) {
            addText(
                    commands,
                    line,
                    fontSize,
                    alignment
            );
        }
    }

    private void addText(
            List<ReceiptCommand> commands,
            String text,
            float fontSize,
            Alignment alignment
    ) {
        commands.add(
                new ReceiptCommand(
                        CommandType.TEXT,
                        text,
                        null,
                        fontSize,
                        fontSize + 1.5f,
                        alignment
                )
        );
    }

    private void addRow(
            List<ReceiptCommand> commands,
            String leftText,
            String rightText,
            float fontSize
    ) {
        commands.add(
                new ReceiptCommand(
                        CommandType.ROW,
                        leftText,
                        rightText,
                        fontSize,
                        fontSize + 1.5f,
                        Alignment.LEFT
                )
        );
    }

    private void addSeparator(
            List<ReceiptCommand> commands
    ) {
        commands.add(
                new ReceiptCommand(
                        CommandType.SEPARATOR,
                        null,
                        null,
                        0f,
                        5f,
                        Alignment.LEFT
                )
        );
    }

    private void addSpacer(
            List<ReceiptCommand> commands,
            float height
    ) {
        commands.add(
                new ReceiptCommand(
                        CommandType.SPACER,
                        null,
                        null,
                        0f,
                        height,
                        Alignment.LEFT
                )
        );
    }

    private List<String> wrapText(
            String originalText,
            PDFont font,
            float fontSize,
            float maximumWidth
    ) throws IOException {

        String text = normalizeWhitespace(originalText);

        if (text.isBlank()) {
            return List.of("");
        }

        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String candidate = currentLine.isEmpty()
                    ? word
                    : currentLine + " " + word;

            if (textWidth(font, candidate, fontSize) <= maximumWidth) {
                currentLine.setLength(0);
                currentLine.append(candidate);
                continue;
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
            }

            if (textWidth(font, word, fontSize) <= maximumWidth) {
                currentLine.append(word);
                continue;
            }

            List<String> pieces = breakLongWord(
                    word,
                    font,
                    fontSize,
                    maximumWidth
            );

            for (int index = 0; index < pieces.size(); index++) {
                String piece = pieces.get(index);

                if (index == pieces.size() - 1) {
                    currentLine.append(piece);
                } else {
                    lines.add(piece);
                }
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private List<String> breakLongWord(
            String word,
            PDFont font,
            float fontSize,
            float maximumWidth
    ) throws IOException {

        List<String> pieces = new ArrayList<>();
        StringBuilder currentPiece = new StringBuilder();

        for (char character : word.toCharArray()) {
            String candidate =
                    currentPiece.toString() + character;

            if (
                    !currentPiece.isEmpty()
                            && textWidth(
                            font,
                            candidate,
                            fontSize
                    ) > maximumWidth
            ) {
                pieces.add(currentPiece.toString());
                currentPiece.setLength(0);
            }

            currentPiece.append(character);
        }

        if (!currentPiece.isEmpty()) {
            pieces.add(currentPiece.toString());
        }

        return pieces;
    }

    private float textWidth(
            PDFont font,
            String text,
            float fontSize
    ) throws IOException {

        if (text == null || text.isEmpty()) {
            return 0f;
        }

        return font.getStringWidth(text)
                / 1000f
                * fontSize;
    }

    private String localizedProductName(
            String originalName,
            String language
    ) {
        String cleanedName = plainText(originalName);

        int separatorIndex =
                cleanedName.indexOf(" / ");

        if (separatorIndex < 0) {
            return cleanedName;
        }

        if ("en".equals(language)) {
            return cleanedName
                    .substring(separatorIndex + 3)
                    .trim();
        }

        return cleanedName
                .substring(0, separatorIndex)
                .trim();
    }

    private String plainText(String value) {
        if (value == null) {
            return "";
        }

        String withoutTags = value.replaceAll(
                "(?s)<[^>]*>",
                ""
        );

        String unescaped =
                HtmlUtils.htmlUnescape(withoutTags);

        return normalizeWhitespace(unescaped);
    }

    private String normalizeWhitespace(String value) {
        return value
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String translate(
            String language,
            String bulgarian,
            String english
    ) {
        return "en".equals(language)
                ? english
                : bulgarian;
    }

    private String normalizeLanguage(String language) {
        return "en".equalsIgnoreCase(language)
                ? "en"
                : "bg";
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            return "0.00 €";
        }

        return value
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString()
                + " €";
    }

    private enum CommandType {
        TEXT,
        ROW,
        SEPARATOR,
        SPACER
    }

    private enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    private record ReceiptCommand(
            CommandType type,
            String leftText,
            String rightText,
            float fontSize,
            float advance,
            Alignment alignment
    ) {
    }
}