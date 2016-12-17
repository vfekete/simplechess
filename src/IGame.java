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

import java.util.List;

/**
 *
 * @author Vladimir Fekete
 */
public interface IGame {

    /**
     * possible states of the game
     */
    enum State {
        NotStarted, BlackOnMove, WhiteOnMove, ChessMate
    };

    /**
     * Starts new game with given figure composition
     *
     * @param figures figures composition
     * @param startingState state of the game
     */
    void start(List<Figure> figures, State startingState);

    /**
     * starts new game with default figures composition
     */
    void startNew();

    /**
     * returns current list of figures (copy)
     *
     * @return current list of figures in the game
     */
    List<Figure> getFigures();

    /**
     * current game state
     *
     * @return game state
     */
    State getState();
}
