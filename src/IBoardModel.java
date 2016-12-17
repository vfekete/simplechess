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
import java.util.List;

/**
 *
 * @author Vladimir Fekete
 */
public interface IBoardModel {

    /**
     * Return model as list of figures in the game.
     *
     * @return List of figures in the game.
     */
    List<Figure> toList();

    /**
     * Perform move on figure located at start moving to end. Method just moves
     * the figure. It has no other functionality.
     *
     * @param start Start location of the figure.
     * @param end End location of the figure.
     * @return True if figure moved, false otherwise.
     */
    boolean moveFigure(Point start, Point end);

    /**
     * Sets IsSelected value of given figure. Method takes no other action.
     *
     * @param position position on which figure should reside.
     * @param isSelected Value of selection property.
     */
    public void markFigureSelected(Point position, boolean isSelected);

    /**
     * Force table cell redraw.
     *
     * @param position redraw cell on this position
     */
    public void forceRedraw(Point position);

    /**
     * Force redraw whole table
     */
    public void forceRedrawAll();
    
    /**
     * returns true if chess mate happened
     */
    public boolean isChessMate();
}
