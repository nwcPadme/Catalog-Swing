package cz.vhromada.catalog.gui.common;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * A class represents table cell renderer for changing alignment of the cell.
 *
 * @author Vladimir Hromada
 */
public final class StatsTableCellRenderer extends DefaultTableCellRenderer {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of StatsTableCellRenderer.
     */
    public StatsTableCellRenderer() {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }

}
