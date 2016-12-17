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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vladimir Fekete
 */
public class GameReader {

    public GameReader() {
        this.figures = null;
    }

    public void loadFromFile(Path filePath) {
        this.isRead = false;

        List<String> lines;

        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            return;
        }

        if (lines == null || lines.size() != 13) {
            return;
        }

        figures = new ArrayList<>();

        gameState = Integer.parseInt(lines.get(0));

        int pos = 1;

        Class<?>[] figureTypes = {FigurePawn.class, FigureRook.class, FigureKnight.class, FigureBishop.class, FigureQueen.class, FigureKing.class};

        for (int i = 0; i < 2; i++) {

            boolean isBlack = (0 == (i % 2));

            try {
                for (Class figureType : figureTypes) {
                    this.setPosition(lines.get(pos++), isBlack, figures, figureType);
                }
            } catch (Exception e) {
                return;
            }
        }

        this.isRead = true;
    }

    List<Figure> getFigures() {
        return this.figures;
    }

    IGame.State getGameState() {
        switch (this.gameState) {
            case 0:
                return IGame.State.BlackOnMove;
            case 1:
                return IGame.State.WhiteOnMove;
            case 2:
                return IGame.State.ChessMate;
            default:
                return IGame.State.NotStarted;
        }
    }

    boolean getIsRead() {
        return this.isRead;
    }

    private <T extends Figure> void setPosition(String posStr, boolean isBlack, List<Figure> list, Class<T> type) throws Exception {
        String[] tokens = posStr.split("");

        if ((tokens.length % 2) != 0) {
            return;
        }

        try {
            for (int i = 0; i < tokens.length; i += 2) {
                int column = Integer.parseInt(tokens[i]);
                int row = Integer.parseInt(tokens[i + 1]);

                T figure = type.cast(Class.forName(type.getName()).getConstructor(boolean.class, Point.class).newInstance(isBlack, new Point(row, column)));
                list.add(figure);
            }
        } catch (NumberFormatException | InstantiationException | IllegalAccessException e) {
        }
    }

    private int gameState = -1;
    private boolean isRead = false;
    private List<Figure> figures;
}
