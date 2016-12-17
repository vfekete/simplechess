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


/**
 * Can be extended with methods such as getAllPossibleMoves
 *
 * @author Vladimir Feketefekete
 */
public interface IFigureAction {

    /**
     * Checks whether this figure can move from it position to target position
     *
     * @param board all figures in game
     * @param targetPosition target position
     * @return true if can, false if cannot
     */
    boolean canMove(IBoardModel board, Point targetPosition);
}
