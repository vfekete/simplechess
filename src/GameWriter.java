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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Vladimir Fekete
 */
public class GameWriter {

    public void writeToFile(Path filePath, IGame.State gameState, List<Figure> figures) {

        if (figures == null) {
            return;
        }

        try {
            String path = filePath.toString();

            if (path.endsWith(".scgame") == false) {
                path += ".scgame";
            }

            File file = new File(path);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            switch (gameState) {
                case BlackOnMove:
                    writer.write("0");
                    break;
                case WhiteOnMove:
                    writer.write("1");
                    break;
                case ChessMate:
                    writer.write("2");
                    break;
                default:
                    writer.write("3");
            }

            writer.newLine();

            Class<?>[] figureTypes = {FigurePawn.class, FigureRook.class, FigureKnight.class, FigureBishop.class, FigureQueen.class, FigureKing.class};

            for (int i = 0; i < 2; i++) {
                boolean isBlack = (0 == (i % 2));

                for (Class figureType : figureTypes) {
                    writer.write(getPositionAsString(figureType, isBlack, figures));
                    writer.newLine();
                }
            }

            writer.close();

        } catch (Exception e) {
            return;
        }

        isWritten = true;
    }

    public boolean getIsWritten() {
        return isWritten;
    }

    private <T extends Figure> String getPositionAsString(Class<T> type, boolean isBlack, List<Figure> allFigures) {

        List<Figure> figures = allFigures.stream().filter(f -> f.getClass() == type && f.getIsBlack() == isBlack).collect(Collectors.toList());

        String strPos = "";

        for (Figure f : figures) {
            Point pos = f.getPosition();
            strPos += pos.y;
            strPos += pos.x;
        }

        return strPos;
    }

    private boolean isWritten = false;
}
