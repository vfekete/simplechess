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
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Vladimir Fekete
 */
public class FigurePawn extends Figure implements IFigureAction {

    public FigurePawn(boolean isBlack) {
        super(isBlack);
    }

    public FigurePawn(boolean isBlack, Point position) {
        super(isBlack);
        this.setPosition(position);
    }

    //
    //  Figure base class
    //
    @Override
    public char getName() {
        return 'P';
    }

    @Override
    protected void setAvatar() {
        avatar = (isBlack == true ? "♟" : "♙");
    }

    //
    //  interface IFigureAction
    //
    @Override
    public boolean canMove(IBoardModel board, Point targetPosition) {

        // nejdeme nahodou mimo grid?
        if (targetPosition.x < 0 || targetPosition.y < 0
                || targetPosition.x > 7 || targetPosition.y > 7) {
            return false;
        }

        // nejdem nahodou dozadu?
        if ((this.isBlack == true && this.position.y >= targetPosition.y)
                || (this.isBlack == false && this.position.y <= targetPosition.y)) {
            return false;
        }

        int diff = Math.abs(this.position.y - targetPosition.y);

        // explicitne podporujeme len: 
        // pesiak sa musi hybat a moze sa len o 1, alebo 2 policka
        if (diff != 1 && diff != 2) {
            return false;
        }

        List<Figure> allFigures = board.toList();

        // pesiak ma povolene pohnut sa o 2 len ked je v zaciatocnej polohe
        // a pohnut sa moze len dopredu
        if (diff == 2) {

            if ((this.isBlack == true && this.position.y != 1)
                    || (this.isBlack == false && this.position.y != 6)
                    || (this.position.x != targetPosition.x)) {
                return false;
            }

            int row1 = 2;
            int row2 = 3;

            if (this.isBlack == false) {
                row1 = 5;
                row2 = 4;
            }

            // stoji mi nieco v ceste?
            for (Figure figure : allFigures) {

                Point pos = figure.getPosition();

                if ((pos.y == row1 || pos.y == row2) && pos.x == targetPosition.x) {
                    return false;
                }
            }

            // go go go
            return true;
        } else if (this.position.x == targetPosition.x) {

            //  Hybem sa hore/dole, nesmie mi nic stat v ceste
            for (Figure figure : allFigures) {
                Point pos = figure.getPosition();

                if (pos.y == targetPosition.y && pos.x == targetPosition.x) {
                    // mame nieco v ceste
                    return false;
                }
            }

            // go go go
            return true;
        }

        // cize uz len vyhadzujeme       
        // kedze v tomto momente je diff == 1 a target poistion x je ina
        Figure targetFigure = null;

        for (Figure figure : allFigures) {
            Point pos = figure.getPosition();

            if (pos.y == targetPosition.y && pos.x == targetPosition.x) {
                targetFigure = figure;
                break;
            }
        }

        // ked idem vyhadzovat, musi tam byt nieco,
        // nesmie to byt kral a
        // nesmie to mat rovnaku farbu ako mam ja
        if (targetFigure == null
                || targetFigure.getClass() == FigureKing.class
                || targetFigure.getIsBlack() == this.isBlack) {
            return false;
        }

        // ok, mozeme ho vyhodit
        return true;
    }
}
