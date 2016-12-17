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
 * base class for every figure
 *
 * @author Vladimir Fekete
 */
public abstract class Figure {

    public Figure(boolean isBlack) {
        this.isBlack = isBlack;
        this.setAvatar();
    }

    final boolean getIsBlack() {
        return this.isBlack;
    }

    final String getAvatar() {
        return this.avatar;
    }

    final boolean getIsSelected() {
        return this.isSelected;
    }

    final void setIsSelected(boolean value) {
        this.isSelected = value;
    }

    final void setPosition(Point position) {
        if (position.x < 0 || position.x > 7
                || position.y < 0 || position.y > 7) {
            return;
        }

        this.position = position;
    }

    final Point getPosition() {
        return this.position;
    }

    //
    //  abstract methods
    //
    public abstract char getName();

    protected abstract void setAvatar();

    //
    //  fields
    // 
    protected boolean isBlack = false;

    protected String avatar = "";

    protected Point position = new Point(-1, -1);

    private boolean isSelected = false;
}
