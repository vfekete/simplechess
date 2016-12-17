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
public class FigureKing extends Figure implements IFigureAction {

    public FigureKing(boolean isBlack) {
        super(isBlack);
    }

    public FigureKing(boolean isBlack, Point position) {
        super(isBlack);
        this.setPosition(position);
    }
    
    public boolean getIsInChess(IBoardModel board)
    {        
        if (board == null) {
            return false;
        }
        
        List<Figure> allFigures = board.toList();
        
        return this.getIsPointInThreat(allFigures, this.position);
    }
    
    public boolean getIsInChessMate(IBoardModel board)
    {
        List<Point> validPositions = getValidPositions(this.position);
        
        for(Point p : validPositions) {
            if (this.canMove(board, p) == true) {
                return false;
            }
        }                
        
        return true;
    }    

    @Override
    public char getName() {
        return 'K';
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
        
        // toto su mozne sposogby ako sa mozem pohnut        
        List<Point> validPositions = getValidPositions(this.position);
        
        // je pozicia kam chcem ist validna?
        if (validPositions.stream().anyMatch( p -> p.x == targetPosition.x && p.y == targetPosition.y) == false) {
            return false;
        }
        
        List<Figure> allFigures = board.toList();
        
        Figure f = getFigureAtPos(allFigures, targetPosition);
        
        // ak tam kam sa chcem pohnut niekto je a ma rovnaku farbu, alebo je to kral, tak sa nemozem pohnut
        if (f != null && f.getIsBlack() == this.isBlack) { 
            return false;
        }
        
        // nemozem sa tam pohnut, cudzi kral je blizko
        if (getIsOpponentKingAround(allFigures, targetPosition) == true) {
            return false;
        }
        
        if (getIsPointInThreat(allFigures, targetPosition)) {
            return false;
        }        
        
        return true;
    }

    @Override
    protected void setAvatar() {
        if (isBlack) {
            avatar = "♚";
        } else {
            avatar = "♔";
        }
    }
    
    private List<Point> getValidPositions(Point point) {
        List<Point> pos = new ArrayList<>();
        
        pos.add( new Point(point.x - 1, point.y - 1));
        pos.add( new Point(point.x, point.y - 1));
        pos.add( new Point(point.x + 1, point.y - 1));
        
        pos.add( new Point(point.x - 1, point.y));
        pos.add( new Point(point.x + 1, point.y));
        
        pos.add( new Point(point.x - 1, point.y + 1));
        pos.add( new Point(point.x, point.y + 1));
        pos.add( new Point(point.x + 1, point.y + 1));
        
        return pos.stream().filter(p -> p.x > -1 && p.x < 8 && p.y > -1 && p.y < 8).collect(Collectors.toList());
    }
        
    private boolean getIsOpponentKingAround(List<Figure> allFigures, Point targetPosition) {
        // zisti mi kde sa nachadza protivnikov kral
        List<Figure> figures = allFigures.stream().filter( f -> f.getClass() == FigureKing.class && f.getIsBlack() != this.isBlack).collect(Collectors.toList());
        
        if (figures == null || figures.size() != 1) {
            // nieco zle sa stalo, neviem najst protivnikoveho krala v hre
            return false;
        }
        
        Figure oKing = figures.get(0);
        
        // je to kam chceme ist v jednotkovom okoli cudizeho krala?
        if ((Math.abs(oKing.getPosition().x - targetPosition.x) == 1) &&
            Math.abs(oKing.getPosition().y - targetPosition.y) == 1) {
            return true;
        }                
        
        return false;
    }
       
    private boolean getIsPointInThreat(List<Figure> allFigures, Point point) {                        
        // toto je vlemi skaredo napisana metoda, za normalnych okolnosti by sa po zamysleni mala dat rozumnejsie zrefaktorovat. Toto je narychlop, aby to bolo funkcne
        
        List<Point> points = new ArrayList<>();
                      
        //
        //  smery ako veza
        //
               
        for(int i=point.x - 1; i>-1; i--) {
            Figure f = getFigureAtPos(allFigures, new Point(i, point.y));
            
            if (f != null && f.getIsBlack() != this.isBlack && 
                (f.getClass() == FigureRook.class || f.getClass() == FigureQueen.class)) {
                return true;
            }
        }
        
        for(int i=point.x + 1; i<8; i++) {
            Figure f = getFigureAtPos(allFigures, new Point(i, point.y));
            
            if (f != null && 
                f.getIsBlack() != this.isBlack &&
                (f.getClass() == FigureRook.class || f.getClass() == FigureQueen.class)) {
                return true;
            }            
        }
        
        for(int i=point.y - 1; i>-1; i--) {
            Figure f = getFigureAtPos(allFigures, new Point(point.x, i));
            
            if (f != null && f.getIsBlack() != this.isBlack &&
                (f.getClass() == FigureRook.class || f.getClass() == FigureQueen.class))  {
                return true;
            }            
        }
        
        for(int i=point.y + 1; i<8; i++) {
            Figure f = getFigureAtPos(allFigures, new Point(point.x, i));
            
            if (f != null && f.getIsBlack() != this.isBlack &&
                (f.getClass() == FigureRook.class || f.getClass() == FigureQueen.class))  {
                return true;
            }            
        }
        
        //
        //  smery ako strelec
        // 
        
        // do laveho horneho rohu
        for(int i = point.x - 1; i > -1; i--) {
            int posX = i;
            int posY = point.y - (point.x - i);
            
            if (posY < 0) {
                break;
            }
            
            Figure f = getFigureAtPos(allFigures, new Point(posX, posY));
            
            if (f != null && f.getIsBlack() != this.isBlack && 
               (f.getClass() == FigureBishop.class || f.getClass() == FigureQueen.class)) {
                return true;
            }                        
        }
        
        
        // do praveho horneho rohu
        for(int i=point.x + 1; i < 8; i++) {
            int posX = i;
            int posY = point.y - (point.x - i);
            
            if (posY < 0) {
                break;
            }
            
            Figure f = getFigureAtPos(allFigures, new Point(posX, posY));
            
            if (f != null && f.getIsBlack() != this.isBlack && 
               (f.getClass() == FigureBishop.class || f.getClass() == FigureQueen.class)) {
                return true;
            }
        }
        
        // do laveho dolneho rohu
        for(int i=point.x - 1; i>-1; i--) {
            int posX = i;
            int posY = point.y + (point.x - i);
            
            if (posY > 7) {
                break;
            }
            
            Figure f = getFigureAtPos(allFigures, new Point(posX, posY));
            
            if (f != null && f.getIsBlack() != this.isBlack && 
               (f.getClass() == FigureBishop.class || f.getClass() == FigureQueen.class)) {
                return true;
            }            
        }
        
        // do praveho dolneho rohu
        for(int i=point.x + 1; i<8; i++) {
            int posX = i;
            int posY = point.y + (point.x - i);
            
            if (posY > 7) {
                break;
            }
            
            Figure f = getFigureAtPos(allFigures, new Point(posX, posY));
            
            if (f != null && f.getIsBlack() != this.isBlack && 
               (f.getClass() == FigureBishop.class || f.getClass() == FigureQueen.class)) {
                return true;
            }            
        }        
        
        // podstatne jednotkove okolie - pesiaci
        Figure f = null;
        
        f = getFigureAtPos(allFigures, new Point(point.x - 1, point.y - 1));
        if (f != null && f.getClass() == FigurePawn.class && f.getIsBlack() == true && this.isBlack == false) {
            return true;
        }
        
        f = getFigureAtPos(allFigures, new Point(point.x + 1, point.y - 1));
        if (f != null && f.getClass() == FigurePawn.class && f.getIsBlack() == true && this.isBlack == false) { // ta ista podmienka
            return true;
        }
        
        f = getFigureAtPos(allFigures, new Point(point.x - 1, point.y + 1));
        if (f != null && f.getClass() == FigurePawn.class && f.getIsBlack() == false && this.isBlack == true) {
            return true;
        }
        
        f = getFigureAtPos(allFigures, new Point(point.x + 1, point.y + 1));
        if (f != null && f.getClass() == FigurePawn.class && f.getIsBlack() == false && this.isBlack == true) {
            return true;
        }                                
        
        // kone
        List<Point> horseStartPosition = getValidKnightMoves(point);
        
        // budu najviac 2
        List<Figure> horses = allFigures.stream().filter( fig -> fig.getClass() == FigureKnight.class && fig.getIsBlack() != this.isBlack ).collect(Collectors.toList());
        
        if (horses != null) {
            for(Point horsePosition : horseStartPosition) {
                if (horses.stream().anyMatch( fig -> fig.getPosition().x == horsePosition.x && fig.getPosition().y == horsePosition.y) == true) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private Figure getFigureAtPos(List<Figure> allFigures, Point pos)
    {
        if (pos.x < 0 || pos.y < 0 || pos.x > 7 || pos.y > 7) {
            return null;
        }
        
        for(Figure f : allFigures) {
            Point fPosition = f.getPosition();
            
            if (fPosition.x == pos.x && fPosition.y == pos.y) {
                return f;
            }
        }                
        return null;
    }    
    
    private List<Point> getValidKnightMoves(Point target) {
        List<Point> points = new ArrayList<>();
        
        points.add(new Point(target.x - 1, target.y - 2));
        points.add(new Point(target.x + 1, target.y - 2));
        
        points.add(new Point(target.x + 2, target.y - 1));
        points.add(new Point(target.x + 2, target.y + 1));
        
        points.add(new Point(target.x - 1, target.y + 2));
        points.add(new Point(target.x + 1, target.y + 2));
        
        points.add(new Point(target.x - 2, target.y - 1));
        points.add(new Point(target.x + 2, target.y + 1));
        
        return points.stream().filter( p -> p.x > -1 && p.y > -1 && p.x < 8 && p.y < 8).collect(Collectors.toList());
    }
}
