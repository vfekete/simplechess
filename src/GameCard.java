/*
 *  This file is part of SimpleChess.
 *  Copyright (C) 2016, Vladimir Fekete
 *
 *  SimpleChess is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SimpleChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Vladimir Fekete
 */
public class GameCard extends javax.swing.JPanel implements ICard, IGame {

    public GameCard() {
        InitializeTable();
        InitializePanel();
    }

    //
    //  interface ICard
    //
    @Override
    public String getName() {
        return "GameCard";
    }

    //
    //  interface IGame
    //
    @Override
    final public void start(List<Figure> figures, State startingState) {
        this.board = new BoardModel(figures);
        this.boardTable.setModel(this.board);
        this.gameState = startingState;
    }

    @Override
    final public void startNew() {
        List figures = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            figures.add(new FigurePawn(true, new Point(i, 1)));
            figures.add(new FigurePawn(false, new Point(i, 6)));
        }

        figures.add(new FigureRook(true, new Point(0, 0)));
        figures.add(new FigureKnight(true, new Point(1, 0)));
        figures.add(new FigureBishop(true, new Point(2, 0)));
        figures.add(new FigureQueen(true, new Point(3, 0)));
        figures.add(new FigureKing(true, new Point(4, 0)));
        figures.add(new FigureBishop(true, new Point(5, 0)));
        figures.add(new FigureKnight(true, new Point(6, 0)));
        figures.add(new FigureRook(true, new Point(7, 0)));

        figures.add(new FigureRook(false, new Point(0, 7)));
        figures.add(new FigureKnight(false, new Point(1, 7)));
        figures.add(new FigureBishop(false, new Point(2, 7)));
        figures.add(new FigureQueen(false, new Point(3, 7)));
        figures.add(new FigureKing(false, new Point(4, 7)));
        figures.add(new FigureBishop(false, new Point(5, 7)));
        figures.add(new FigureKnight(false, new Point(6, 7)));
        figures.add(new FigureRook(false, new Point(7, 7)));

        this.board = new BoardModel(figures);
        this.boardTable.setModel(this.board);
        this.gameState = IGame.State.WhiteOnMove;
    }

    @Override
    final public List<Figure> getFigures() {
        if (this.board == null) {
            return new ArrayList<Figure>();
        }
        return ((IBoardModel) this.board).toList();
    }

    @Override
    final public State getState() {
        return this.gameState;
    }

    private void InitializePanel() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        Font infoFont = new Font("Dialog", Font.BOLD, 16);
        gameInfo.setFont(infoFont);
        gameInfo.setText("WHITE MOVES");
        gameInfo.setAlignmentX(CENTER_ALIGNMENT);
        gameInfo.setAlignmentY(CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(gameInfo);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(boardTable);
    }

    private void InitializeTable() {
        cellRenderer = new BoardCellRenderer();
        boardTable = new JTable(8, 8);
        boardTable.setDefaultRenderer(Object.class, cellRenderer);
        boardTable.setColumnSelectionAllowed(false);
        boardTable.setRowSelectionAllowed(false);
        boardTable.setCellSelectionEnabled(true);
        boardTable.setRowHeight(50);
        boardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        ListSelectionModel columnSelectionModel = boardTable.getColumnModel().getSelectionModel();

        if (columnSelectionModel != null) {
            columnSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            columnSelectionModel.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {                // https://community.oracle.com/thread/1359855?start=0&tstart=0
                    int row = boardTable.getSelectedRow();
                    int column = boardTable.getSelectedColumn();
                    this.selectedCell(row, column);
                }
            });
        }

        ListSelectionModel rowSelectionModel = boardTable.getSelectionModel();

        if (rowSelectionModel != null) {
            rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            rowSelectionModel.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {                // https://community.oracle.com/thread/1359855?start=0&tstart=0
                    int row = boardTable.getSelectedRow();
                    int column = boardTable.getSelectedColumn();
                    this.selectedCell(row, column);
                }
            });
        }
    }

    private void selectedCell(int row, int column) {
        
        if (this.getState() == gameState.ChessMate ||
            this.getState() == gameState.NotStarted) {
            return;
        }

        // ignorujem duplicitne eventy
        if (row < 0 || column < 0) {
            return;
        }        

        // hra sa vobec?
        if (this.gameState == IGame.State.NotStarted
                || this.gameState == IGame.State.ChessMate) {
            return;
        }

        Figure figure = (Figure) board.getValueAt(row, column);

        // je to prvy vyber, je vobec mozny?
        if (firstSelectionPoint == null
                && (figure == null
                || (figure.getIsBlack() == true && this.gameState == IGame.State.WhiteOnMove)
                || (figure.getIsBlack() == false && this.gameState == IGame.State.BlackOnMove))) {
            boardTable.clearSelection();
            return;
        }

        IBoardModel model = (IBoardModel) this.board;

        if (firstSelectionPoint == null) {
            firstSelectionPoint = new Point(column, row);
            model.markFigureSelected(firstSelectionPoint, true);
            model.forceRedraw(firstSelectionPoint);
        } else {
            model.markFigureSelected(firstSelectionPoint, false);
            Point targetPosition = new Point(column, row);

            boolean ok = model.moveFigure(firstSelectionPoint, targetPosition);
                       
            model.forceRedraw(firstSelectionPoint);
            model.forceRedraw(targetPosition);
            firstSelectionPoint = null;
            
            if (model.isChessMate()) {
                this.gameState = gameState.ChessMate;
                gameInfo.setForeground(Color.red);
                gameInfo.setText("CHESS MATE, GAME OVER!");
                return;
            }            

            if (ok == true) {                
                this.switchSides();
            }
        }
        
        // musim zresetovat poziciu, ktoru editujem, nech som fresh
        boardTable.clearSelection();
    }

    private void switchSides() {

        if (this.gameState != IGame.State.BlackOnMove
                && this.gameState != IGame.State.WhiteOnMove) {
            switch (this.getState()) {
                case NotStarted:
                    gameInfo.setText("START A NEW GAME");
                    break;
                case ChessMate:
                    gameInfo.setText("CHESSMATE");
                    break;
            }
            return;
        }

        if (this.gameState == IGame.State.BlackOnMove) {
            this.gameState = IGame.State.WhiteOnMove;
            this.gameInfo.setText("WHITE MOVES");
        } else {
            this.gameState = IGame.State.BlackOnMove;
            this.gameInfo.setText("BLACK MOVES");
        }
    }

    private BoardModel board;

    private BoardCellRenderer cellRenderer;

    private JTable boardTable = null;

    private final JLabel gameInfo = new JLabel();

    private IGame.State gameState = IGame.State.NotStarted;

    private Point firstSelectionPoint = null;
}
