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
public class FigureKnight extends Figure implements IFigureAction {

    public FigureKnight(boolean isBlack) {
        super(isBlack);
    }

    public FigureKnight(boolean isBlack, Point position) {
        super(isBlack);
        this.setPosition(position);
    }

    @Override
    public char getName() {
        return 'N';
    }

    //
    //  IFigureAction interfaces
    //    
    @Override
    public boolean canMove(IBoardModel board, Point targetPosition) {
        
        // nejdeme nahodou mimo grid?
        if (targetPosition.x < 0 || targetPosition.y < 0
                || targetPosition.x > 7 || targetPosition.y > 7) {
            return false;
        }
        
        // vyrobim si zoznam moznych poloh kona podla aktualnej polohy
        List<Point> validPositions = getValidPositions();        
        
        // ak to kam user klikol ze chce ist nie je medzi validnymi polohami, alebo ak sa neda s konom nikam ist (validPositions = null), tak sa nemozem hybat
        if (validPositions == null || validPositions.stream().anyMatch(p -> p.x == targetPosition.x && p.y == targetPosition.y) == false) {
            return false;
        }
        
        List<Figure> allFigures = board.toList();
        
        // zoznam (v skutocnosti stale najviac o 1 prvku) figurok, ktore su na target pozicii
        List<Figure> figures = allFigures.stream().filter( f -> f.getPosition().x == targetPosition.x && f.getPosition().y == targetPosition.y).collect(Collectors.toList());
        
        
        //policko je prazdne, mozem sa pohnut
        if (figures.isEmpty() == true) {
            return true;
        }        
        
        // policko nie je prazdne. mozem sa pohnut iba vtedy, ak tam nie je kral (ten sa neda vyhodit) a zaroven je tam nieco s roznou farbou
        return !(figures.get(0).isBlack == this.isBlack ||
                figures.get(0).getClass() == FigureKing.class);
    }

    @Override
    protected void setAvatar() {
        if (isBlack) {
            avatar = "♞";
        } else {
            avatar = "♘";
        }
    }
    
    private List<Point> getValidPositions()
    {
        List<Point> validPositions = new ArrayList<>();
        
        validPositions.add(new Point(this.position.x - 1, this.position.y - 2));
        validPositions.add(new Point(this.position.x + 1, this.position.y - 2));
        
        validPositions.add(new Point(this.position.x - 2, this.position.y - 1));
        validPositions.add(new Point(this.position.x - 2, this.position.y + 1));
        
        validPositions.add(new Point(this.position.x - 1, this.position.y + 2));
        validPositions.add(new Point(this.position.x + 1, this.position.y + 2));
        
        validPositions.add(new Point(this.position.x - 2, this.position.y - 1));
        validPositions.add(new Point(this.position.x - 2, this.position.y + 1));
              
        return validPositions.stream().filter( p -> p.x > -1 && p.y > -1 && p.x < 8 && p.y < 8).collect(Collectors.toList());
    }
}
