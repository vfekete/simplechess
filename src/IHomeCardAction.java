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

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Vladimir Fekete
 */
public interface IHomeCardAction {

    /**
     * orders game card to start a new default game
     */
    void startNewGame();

    /**
     * orders game card to load a game
     *
     * @param filePath path to stored game
     */
    void loadGameFromFile(Path filePath);

    /**
     * orders game card to save game
     *
     * @param filePath path where game will be stored
     */
    void saveGameToFile(Path filePath);
}
