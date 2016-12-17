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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Dummy renderer, just colors the cells, no decision making is done here.
 *
 * @author fekete
 */
public class BoardCellRenderer extends JLabel implements TableCellRenderer {

    public BoardCellRenderer() {
        this.setOpaque(true);
    }

    //
    //  TableCellRenderer interface
    //
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        this.initializeCell(row, column);

        Figure figure = (Figure) value;

        if (figure == null) {
            return this;
        }

        this.setText(figure.getAvatar());

        if (figure.getIsSelected() == true) {
            this.setForeground(selectedColor);
        }

        return this;
    }

    private void initializeCell(int row, int column) {
        if (((row % 2) == 0 && ((column + 1) % 2) == 0)
                || ((row % 2) != 0 && (column % 2 == 0))) {
            this.setBackground(blackBoardColor);
        } else {
            this.setBackground(whiteBoardColor);
        }

        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.setForeground(Color.BLACK);
        this.setSize(42, 42);
        this.setFont(this.font);
        this.setText("");
    }

    final private Color blackBoardColor = Color.decode("#D18B47");
    final private Color whiteBoardColor = Color.decode("#FFCE9E");
    final private Color selectedColor = Color.decode("#00FF00");
    final private Font font = new Font("Dialog", Font.PLAIN, 42);
}
