package com.chess;

import com.chess.movement.*;
import com.chess.pieces.King;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Rook;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


    public void start(Stage stage) throws Exception {
        chessboard.setOnMouseClicked(this::handleMouseClick);

        String filePath = "saved_game.ser";

        // Create the save and load buttons
        Button saveButton = createSaveGameButton(filePath);
        Button loadButton = createLoadGameButton(filePath);

        // Create the undo button
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(event -> undoMove());

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> startNewGame());


        // Wrap the undo button in an HBox with padding
        HBox ButtonWrapper = new HBox(saveButton, loadButton, undoButton, newGameButton);
        ButtonWrapper.setPadding(new Insets(10, 10, 10, 10));
        ButtonWrapper.setSpacing(20);

        // Create a VBox to hold the chessboard and the undo button
        VBox mainLayout = new VBox(chessboard, ButtonWrapper);


        Scene scene = new Scene(mainLayout, 400, 450);
        stage.setTitle("Chess Game");
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

            // Phong cấp
            if (((Pawn) currentSquare.getPiece()).canBePromote(currentSquare)) {
                currentSquare.setPiece(new Rook(currentSquare.getPiece().isWhite()));
                board.resetGUI(chessboard);
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


            if (isEnPassantMove) {
                PawnMove move = new PawnMove(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                move.setEnPassantMove(isEnPassantMove);
                move.setLeftEnPassant(leftEnPassant);
                board.push(move);
            } else if (isCastlingMove) {
                KingMove move = new KingMove(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                move.setCastlingMove(isCastlingMove);
                board.push(move);
            } else {
                Move move = new Move(previousSquare, currentSquare, previousSquare.getPiece(), capturedPiece);
                board.push(move);
            }

            // Kiểm tra nếu sau nước đi tướng vẫn bị chiếu
            if (status.isCheck(!whiteTurn)) {
                System.out.println("Invalid move");
                undoMove();
            }

            // Kiểm tra nếu đã chiếu hết
            if (status.isCheck(!whiteTurn) &&
                status.isCheckmate(!whiteTurn)) {
                String side = whiteTurn ? "White" : "Black";
                System.out.println(side + " win!");
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

            //Xử lý nếu hoàn tác nước đi en passant
            if (lastMove instanceof PawnMove) {
                if (((PawnMove) lastMove).isEnPassantMove()) {
                    if (((PawnMove) lastMove).isLeftEnPassant()) {
                        board.getLeftSquare(lastMove.getStart()).setPiece(new Pawn(!whiteTurn));
                    } else {
                        board.getRightSquare(lastMove.getStart()).setPiece(new Pawn(!whiteTurn));
                    }
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
        board.resetGUI(chessboard);
    }

    public void saveGame(String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(board);
            // Add more data if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Game
    public void loadGame(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            board = (Board) inputStream.readObject();
            board.resetGUI(chessboard);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save Game Button
    public Button createSaveGameButton(String filePath) {
        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(event -> saveGame(filePath));
        return saveButton;
    }

    // Load Game Button
    public Button createLoadGameButton(String filePath) {
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(event -> loadGame(filePath));
        return loadButton;
    }

}
