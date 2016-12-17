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

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * Why abstractiontablemodel? :
 * http://docs.oracle.com/javase/7/docs/api/javax/swing/table/AbstractTableModel.html
 *
 * @author Vladimir Fekete
 */
public class BoardModel extends AbstractTableModel implements IBoardModel {

    BoardModel(List<Figure> startPosition) {
        this.board = startPosition;
        this.listeners = new HashSet<>();
    }

    //
    //  AstractTableModel
    //
    @Override
    public int getRowCount() {
        return 8;
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex > 7) {
            return "";
        }
       
        String columnName = Integer.toString(columnIndex);
        return columnName;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Figure.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        for (Figure f : this.board) {
            Point position = f.getPosition();
            if (position.x == columnIndex
                    && position.y == rowIndex) {
                return f;
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Figure figure = (Figure) this.getValueAt(rowIndex, columnIndex);
        this.board.remove(figure);

        if (aValue != null) {
            this.board.add((Figure) aValue);
        }

        for (TableModelListener l : this.listeners) {
            l.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }

    //
    //  IBoardModel
    //
    @Override
    public List<Figure> toList() {
        List figures = new ArrayList<Figure>(this.board);
        return figures;
    }

    @Override
    public boolean moveFigure(Point start, Point end) {
        Figure figureToMove = (Figure) this.getValueAt(start.y, start.x);
        Figure figureAtEnd = (Figure) this.getValueAt(end.y, end.x);

        if (figureToMove == null) {
            return false;
        }
        
        if (figureToMove.getClass() == FigureKing.class && ((FigureKing)figureToMove).getIsInChessMate((IBoardModel)this)) {
            this.chessMate = true;
            return false;
        }

        if (((IFigureAction) figureToMove).canMove((IBoardModel) this, end) == true) {
            figureToMove.setPosition(end);
            if (figureAtEnd != null) {
                this.board.remove(figureAtEnd);
            }
            return true;
        }
        
        return false;
    }

    @Override
    public void markFigureSelected(Point position, boolean isSelected) {
        Figure figure = (Figure) this.getValueAt(position.y, position.x);

        if (figure != null) {
            figure.setIsSelected(isSelected);
        }
    }

    @Override
    public void forceRedraw(Point position) {
        for (TableModelListener l : this.listeners) {
            TableModelEvent e = new TableModelEvent(this, position.y, position.y, position.x, TableModelEvent.UPDATE);
            l.tableChanged(e);
        }
    }

    @Override
    public void forceRedrawAll() {
        this.fireTableDataChanged();
    }
    
    @Override
    public boolean isChessMate() {
        return this.chessMate;
    }

    //
    //  protected and private stuff
    //
    private final List<Figure> board;

    private final Set<TableModelListener> listeners;
    
    private boolean chessMate = false;
}
