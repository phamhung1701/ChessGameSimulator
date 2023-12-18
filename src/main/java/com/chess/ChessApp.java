package com.chess;

import com.chess.movement.*;
import com.chess.pieces.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;


public class ChessApp extends Application {
    // Tạo bàn cờ và giao diện sử dụng GridPane
    Board board = new Board();
    GridPane chessboard = board.chessboard();
    // Ô đang được chọn
    private Square currentSquare = null;

    // Ô đã chọn trước đó
    private Square previousSquare = null;

    // Lượt đi
    private boolean whiteTurn = true;

    // Tình trạng
    GameStatus status = new GameStatus(board);

    private final String saveFilePath = "chess_save.txt";

    private Label checkmateLabel = new Label("");

    public void start(Stage stage) throws Exception {
        chessboard.setOnMouseClicked(this::handleMouseClick);
        checkmateLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Create the save and load buttons
        Button saveButton = createSaveGameButton();
        Button loadButton = createLoadGameButton();

        // Create the undo button
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(event -> undoMove());

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> startNewGame());


        // Wrap the undo button in an HBox with padding
        HBox ButtonWrapper = new HBox(newGameButton, saveButton, loadButton, undoButton);
        ButtonWrapper.setPadding(new Insets(10, 10, 10, 10));
        ButtonWrapper.setSpacing(35);

        VBox infoLayout = new VBox(checkmateLabel);
        infoLayout.setPadding(new Insets(10, 10, 10, 10));
        infoLayout.setSpacing(20);

        // Create a VBox to hold the chessboard and the undo button
        VBox mainLayout = new VBox(infoLayout, chessboard, ButtonWrapper);


        Scene scene = new Scene(mainLayout, 400, 500);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Xử lý đầu vào dùng chuột
    public void handleMouseClick(MouseEvent event) {
        // Adjust for the resolution and button offset
        double xOffset = (chessboard.getWidth() - chessboard.getBoundsInLocal().getWidth()) / 2;
        double yOffset = (chessboard.getHeight() - chessboard.getBoundsInLocal().getHeight()) / 2;

        // Check if the click is within the chessboard area
        if (event.getX() >= xOffset && event.getY() >= yOffset) {
            // Your existing logic for handling mouse clicks
            int row = (int) ((event.getX() - xOffset) / 50);
            int column = (int) ((event.getY() - yOffset) / 50);
            handleMove(row, column);
        }
    }

    // Xử lý các bước đi
    public void handleMove(int row, int column) {
        //Xác định ô được chọn và ô đã chọn trước đó
        previousSquare = currentSquare;
        currentSquare = this.board.getSquare(row, column);

        if (previousSquare != null) {
            previousSquare.clearHighlight();
        }

        // Kiểm tra trường hợp đặc biệt của quân tốt
        if (currentSquare.getPiece() instanceof Pawn) {
            // En passant bên trái
            if (board.getLeftSquare(currentSquare) != null
                && board.getLeftSquare(currentSquare).getPiece() instanceof Pawn
                && board.getLeftSquare(currentSquare).getPiece().isWhite() != currentSquare.getPiece().isWhite()
                    && ((currentSquare.getRow() == 3 && currentSquare.getPiece().isWhite())
                    || (currentSquare.getRow() == 4 && !currentSquare.getPiece().isWhite()))) {
                ((Pawn) currentSquare.getPiece()).setEnPassantPossibleLeft(true);
                PawnMove enPassant = new PawnMove(board.pop());
                enPassant.setEnPassantPossible(true);
                board.push(enPassant);
            } else {
                ((Pawn) currentSquare.getPiece()).setEnPassantPossibleLeft(false);
            }

            // En passant bên phải
            if (board.getRightSquare(currentSquare) != null
                    && board.getRightSquare(currentSquare).getPiece() instanceof Pawn
                    && board.getRightSquare(currentSquare).getPiece().isWhite() != currentSquare.getPiece().isWhite()
                    && ((currentSquare.getRow() == 3 && currentSquare.getPiece().isWhite())
                    || (currentSquare.getRow() == 4 && !currentSquare.getPiece().isWhite()))) {
                ((Pawn) currentSquare.getPiece()).setEnPassantPossibleRight(true);
                PawnMove enPassant = new PawnMove(board.pop());
                enPassant.setEnPassantPossible(true);
                board.push(enPassant);
            } else {
                ((Pawn) currentSquare.getPiece()).setEnPassantPossibleRight(false);
            }
        }

        // Hiển thị các nước đi hợp lệ cho quân cờ được chọn
        highlightValidMoves(currentSquare);

        // Thực hiện di chuyển các quân cờ
        if (previousSquare != null
            && previousSquare.getPiece() != null
            && previousSquare != currentSquare
            && previousSquare.getPiece().canMove(board, previousSquare, currentSquare)
            && ((whiteTurn && previousSquare.getPiece().isWhite())
                || (!whiteTurn && !previousSquare.getPiece().isWhite()))) {

            checkmateLabel.setText("");



            //Kiểm tra nếu vua đã di chuyển
            if (previousSquare.getPiece() instanceof King) {
                ((King) previousSquare.getPiece()).moved();
            }

            //Kiểm tra nếu xe đã di chuyển
            if (previousSquare.getPiece() instanceof Rook) {
                ((Rook) previousSquare.getPiece()).moved();
            }

            //Kiểm tra nếu nước đi là en passant
            boolean isEnPassantMove = false;
            boolean leftEnPassant = false;
            if (previousSquare.getPiece() instanceof Pawn
                && board.isEnPassantMove(previousSquare, currentSquare)) {
                isEnPassantMove = true;
                if (((Pawn) previousSquare.getPiece()).isEnPassantPossibleLeft()) {
                    leftEnPassant = true;
                    board.getLeftSquare(previousSquare).setPiece(null);
                } else if (((Pawn) previousSquare.getPiece()).isEnPassantPossibleRight()){
                    board.getRightSquare(previousSquare).setPiece(null);
                }
            }

            //Kiểm tra nếu nước đi là nước nhập thành
            boolean isCastlingMove = false;

            if (previousSquare.getPiece() instanceof King
                    && Math.abs(currentSquare.getColumn() - previousSquare.getColumn()) == 2) {
                isCastlingMove = true;
                //King side
                if (currentSquare.getColumn() > previousSquare.getColumn()) {
                    Square destination = board.getSquare(5, currentSquare.getRow());
                    board.getSquare(7, currentSquare.getRow()).movePieceTo(destination);
                }
                //Queen side
                else if (currentSquare.getColumn() < previousSquare.getColumn()) {
                    Square destination = board.getSquare(3, currentSquare.getRow());
                    board.getSquare(0, currentSquare.getRow()).movePieceTo(destination);
                }
            }

            // Di chuyển quân cờ và lưu lại nước đi
            Piece capturedPiece = previousSquare.movePieceTo(currentSquare);

            boolean isPromotionMove = false;
            // Phong cấp
            if (currentSquare.getPiece() instanceof Pawn &&
                    ((Pawn) currentSquare.getPiece()).canBePromote(currentSquare)) {
                showPromotionDialog();
                isPromotionMove = true;
            }

            if (isEnPassantMove) {
                PawnMove move = new PawnMove(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                move.setEnPassantMove(isEnPassantMove);
                move.setLeftEnPassant(leftEnPassant);
                board.push(move);
            } else if (isCastlingMove) {
                KingMove move = new KingMove(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                move.setCastlingMove(isCastlingMove);
                board.push(move);
            } else if (isPromotionMove) {
                PawnMove move = new PawnMove(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                move.setPromotionMove(isPromotionMove);
                board.push(move);
            } else {
                Move move = new Move(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                board.push(move);
            }

            // Kiểm tra nếu sau nước đi tướng vẫn bị chiếu
            if (status.isCheck(whiteTurn)) {
                String side = whiteTurn ? "White" : "Black";
                checkmateLabel.setText(side + " is in check");
                undoMove();
            }

            // Kiểm tra nếu đã chiếu hết
            if (status.isCheckmate(!whiteTurn)) {
                System.out.println("Is checkMate");
                String side = whiteTurn ? "White" : "Black";
                checkmateLabel.setText(side + " win!");
            }

            // Đặt lại giá trị cho các biến
            previousSquare = null;

            // Đổi lượt
            whiteTurn = !whiteTurn;

            // Đặt lại giao diện cho bàn cờ
            board.resetGUI(chessboard);
        }
    }


    //Hàm hiển thị các nước đi hợp lệ của một quân cờ trong một ô xác định
    public void highlightValidMoves(Square start) {
        board.resetGUI(chessboard);
        hightlightSelectedPiece(start.getColumn(), start.getRow());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square end = board.getSquare(i, j);
                if (currentSquare != null && currentSquare.getPiece() != null
                        && currentSquare.getPiece().canMove(board, start, end)
                        && currentSquare.getPiece().isWhite() == this.whiteTurn
                        && status.isMoveValidWithoutCheck(start, end, whiteTurn)) {
                    // Đổi màu của các ô được đánh dấu bằng màu xanh nhạt
                    end.highlight();
                }
            }
        }
    }


    //Hàm hiển thị ô được chọn
    public void hightlightSelectedPiece(int x, int y) {
        board.resetGUI(chessboard);
        board.getSquare(x, y).highlightSelectedPiece();
    }

    //Hoàn tác lại nước đi
    public void undoMove() {
        if (!board.getHistory().isEmpty()) {
            this.whiteTurn = !this.whiteTurn;
            Move lastMove = board.pop();
            checkmateLabel.setText("");

            //Xử lý nếu hoàn tác nước đi của vua
            if (lastMove instanceof KingMove
                    && lastMove.getEnd().getPiece() instanceof King) {
                ((King) lastMove.getEnd().getPiece()).undo();
            }

            //Xử lý nếu hoàn tác nước đi của xe
            if (lastMove.getEnd().getPiece() instanceof Rook) {
                ((Rook) lastMove.getEnd().getPiece()).undo();
            }

            lastMove.getEnd().movePieceTo(lastMove.getStart());
            if (lastMove.getCapturedPiece() != null) {
                lastMove.getEnd().setPiece(lastMove.getCapturedPiece());
            }

            //Xử lý nếu hoàn tác nước đi en passant hoặc phong cấp
            if (lastMove instanceof PawnMove) {
                if (((PawnMove) lastMove).isEnPassantMove()) {
                    if (((PawnMove) lastMove).isLeftEnPassant()) {
                        board.getLeftSquare(lastMove.getStart()).setPiece(new Pawn(!whiteTurn));
                    } else {
                        board.getRightSquare(lastMove.getStart()).setPiece(new Pawn(!whiteTurn));
                    }
                }

                if (((PawnMove) lastMove).isPromotionMove()) {
                    lastMove.getStart().setPiece(new Pawn(whiteTurn));
                }
            }

            // Xử lý nếu hoàn tác nước đi nhập thành
            if (lastMove instanceof KingMove) {
                if (((KingMove) lastMove).isCastlingMove()) {
                    int row = lastMove.getEnd().getRow();
                    int kingStartCol = lastMove.getEnd().getColumn();
                    int kingEndCol = lastMove.getStart().getColumn();

                    // King side
                    if (kingEndCol < kingStartCol) {
                        Square rookSquare = board.getSquare(kingStartCol - 1, row);
                        rookSquare.movePieceTo(board.getSquare(7, row));
                    }
                    // Queen side
                    else if (kingEndCol > kingStartCol) {
                        Square rookSquare = board.getSquare(kingStartCol + 1, row);
                        rookSquare.movePieceTo(board.getSquare(0, row));
                    }
                }
            }
            // Cập nhật giao diện
            board.resetGUI(chessboard);
        }
    }

    public void startNewGame() {
        // Reset the board
        board.resetBoard();

        // Clear move history
        board.clearHistory();

        // Set initial game state
        whiteTurn = true;
        status = new GameStatus(board);

        // Update the GUI
        checkmateLabel.setText("");
        board.resetGUI(chessboard);
    }

    public void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(saveFilePath))) {
            saveBoardState(writer);
            writer.println(whiteTurn ? "WHITE" : "BLACK");
            checkmateLabel.setText("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving the game: " + e.getMessage());
        }
    }

    private void saveBoardState(PrintWriter writer) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = board.getSquare(i, j);
                Piece piece = square.getPiece();
                char pieceSymbol = '-';
                if (piece != null) {
                    pieceSymbol = (piece.isWhite())
                            ? piece.getSymbol() : Character.toLowerCase(piece.getSymbol());
                }
                writer.print(pieceSymbol);
            }
            writer.println();
        }
    }

    public void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFilePath))) {
            loadBoardState(reader);
            String turn = reader.readLine();
            whiteTurn = "WHITE".equals(turn);
            board.resetGUI(chessboard);
            currentSquare = null;
            previousSquare = null;
            checkmateLabel.setText("Game loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading the game: " + e.getMessage());
        }
    }

    private void loadBoardState(BufferedReader reader) throws IOException {
        for (int i = 0; i < 8; i++) {
            String line = reader.readLine();
            for (int j = 0; j < 8; j++) {
                char symbol = line.charAt(j);
                Piece piece = createPiece(symbol);
                board.getSquare(i, j).setPiece(piece);
            }
        }
    }

    private Piece createPiece(char symbol) {
        boolean isWhite = Character.isUpperCase(symbol);

        switch (Character.toUpperCase(symbol)) {
            case 'P':
                return new Pawn(isWhite);
            case 'R':
                return new Rook(isWhite);
            case 'N':
                return new Knight(isWhite);
            case 'B':
                return new Bishop(isWhite);
            case 'Q':
                return new Queen(isWhite);
            case 'K':
                return new King(isWhite);
            default:
                return null;
        }
    }

    public Button createSaveGameButton() {
        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(event -> saveGame());
        return saveButton;
    }

    public Button createLoadGameButton() {
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(event -> loadGame());
        return loadButton;
    }

    public void showPromotionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", "Queen", "Rook", "Bishop", "Knight");
        dialog.setTitle("Pawn Promotion");
        dialog.setHeaderText("Choose a piece to promote your pawn to:");
        dialog.setContentText("Select:");

        // Traditional way to get the response value.
        dialog.showAndWait().ifPresent(promotionChoice -> {
            // Handle the selected promotion choice
            handlePromotion(promotionChoice);
        });
    }

    private void handlePromotion(String promotionChoice) {
        Piece promotedPiece = null;

        switch (promotionChoice) {
            case "Queen":
                promotedPiece = new Queen(currentSquare.getPiece().isWhite());
                break;
            case "Rook":
                promotedPiece = new Rook(currentSquare.getPiece().isWhite());
                break;
            case "Bishop":
                promotedPiece = new Bishop(currentSquare.getPiece().isWhite());
                break;
            case "Knight":
                promotedPiece = new Knight(currentSquare.getPiece().isWhite());
                break;
        }

        // Set the promoted piece on the current square
        currentSquare.setPiece(promotedPiece);

        // Reset the GUI
        board.resetGUI(chessboard);
    }

}
