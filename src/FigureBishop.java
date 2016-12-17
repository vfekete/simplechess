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

/**
 *
 * @author Vladimir Fekete
 */
public class FigureBishop extends Figure implements IFigureAction {

    public FigureBishop(boolean isBlack) {
        super(isBlack);
    }

    public FigureBishop(boolean isBlack, Point position) {
        super(isBlack);
        this.setPosition(position);
    }

    @Override
    public char getName() {
        return 'B';
    }

    //
    //  IFigureAction interface
    //
    @Override
    public boolean canMove(IBoardModel board, Point targetPosition) {
        
        // nejdeme nahodou mimo grid?
        if (targetPosition.x < 0 || targetPosition.y < 0
                || targetPosition.x > 7 || targetPosition.y > 7) {
            return false;
        }                              
        
        // ideme po diagonale?
        if (Math.abs(targetPosition.x - this.position.x) != Math.abs(targetPosition.y - this.position.y)) {
            return false;
        }
                        
        List<Figure> allFigures = board.toList();
        
               
        // vyberiem vsetky prvky, ktore su na diagonale odo mna po to kam chcem skocit
        // seba a ciel do toho neratam
        List<Figure> hinderFigures = new ArrayList<>();
        
        int numOfSteps = Math.abs(this.position.x - targetPosition.x);
        
        int cX = 1;
        int cY = 1;
        
        if (this.position.x < targetPosition.x) {
            cX = -1;
        }
        
        if (this.position.y < targetPosition.y) {
            cY = -1;
        }
        
        for(int i=1; i<numOfSteps; i++) {
            Figure f = getFigureAtPos(allFigures, new Point(targetPosition.x + cX * i, targetPosition.y + cY * i));
            if (f != null) {
                hinderFigures.add(f);
            }
        }
        
        // nieco nam stoji v ceste
        if (hinderFigures.isEmpty() == false) {
            return false;
        }
        
        Figure f = getFigureAtPos(allFigures, targetPosition);
        
        // tam kam idem - ak to existuje a ma to rovnaku farbu ako ja, alebo je to kral
        // tak sa nemozem pohnut
        if (f != null && (f.getIsBlack() == this.isBlack || f.getClass() == FigureKing.class)) {
            return false;
        }
                
        // ok, mozem vyhadzovat (pripadne je to volen policko)
        return true;
    }

    @Override
    protected void setAvatar() {
        if (isBlack) {
            avatar = "♝";
        } else {
            avatar = "♗";
        }
    }
    
    private Figure getFigureAtPos(List<Figure> allFigures, Point pos)
    {
        for(Figure f : allFigures) {
            Point fPosition = f.getPosition();
            
            if (fPosition.x == pos.x && fPosition.y == pos.y) {
                return f;
            }
        }                
        return null;
    }
}
