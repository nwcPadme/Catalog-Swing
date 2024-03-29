package cz.vhromada.catalog.gui.common;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.springframework.util.Assert;

/**
 * An abstract class represents overview panel with data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public abstract class AbstractOverviewDataPanel<T> extends JPanel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal scroll pane size
     */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 200;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 5;

    /**
     * Vertical data component size
     */
    private static final int VERTICAL_DATA_COMPONENT_SIZE = 200;

    /**
     * Vertical size for scroll pane for table with stats
     */
    private static final int VERTICAL_STATS_SCROLL_PANE_SIZE = 45;

    /**
     * Vertical gap size
     */
    private static final int VERTICAL_GAP_SIZE = 2;

    /**
     * Update property
     */
    private static final String UPDATE_PROPERTY = "update";

    /**
     * Popup menu
     */
    private final JPopupMenu popupMenu = new JPopupMenu();

    /**
     * Menu item for adding data
     */
    private final JMenuItem addPopupMenuItem = new JMenuItem("Add", Picture.ADD.getIcon());

    /**
     * Menu item for updating data
     */
    private final JMenuItem updatePopupMenuItem = new JMenuItem("Update", Picture.UPDATE.getIcon());

    /**
     * Menu item for removing data
     */
    private final JMenuItem removePopupMenuItem = new JMenuItem("Remove", Picture.REMOVE.getIcon());

    /**
     * Menu item for duplicating data
     */
    private final JMenuItem duplicatePopupMenuItem = new JMenuItem("Duplicate", Picture.DUPLICATE.getIcon());

    /**
     * Menu item for moving up data
     */
    private final JMenuItem moveUpPopupMenuItem = new JMenuItem("Move up", Picture.UP.getIcon());

    /**
     * Menu item for moving down data
     */
    private final JMenuItem moveDownPopupMenuItem = new JMenuItem("Move down", Picture.DOWN.getIcon());

    /**
     * List with data
     */
    private final JList<String> list = new JList<>();

    /**
     * ScrollPane for list with data
     */
    private final JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Tabbed pane with data
     */
    private final JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Table with with stats
     */
    private final JTable statsTable = new JTable();

    /**
     * ScrollPane for table with stats
     */
    private final JScrollPane statsTableScrollPane = new JScrollPane(statsTable);

    /**
     * Data model for list
     */
    private final AbstractListDataModel<T> listDataModel;

    /**
     * Data model for table with stats
     */
    private final AbstractStatsTableDataModel statsTableDataModel;

    /**
     * True if data is saved
     */
    private boolean saved;

    /**
     * Creates a new instance of AbstractDataPanel.
     *
     * @param listDataModel data model for list
     * @throws IllegalArgumentException if data model for list is null
     */
    public AbstractOverviewDataPanel(final AbstractListDataModel<T> listDataModel) {
        Assert.notNull(listDataModel, "Data model for list mustn't be null.");

        this.listDataModel = listDataModel;
        this.statsTableDataModel = null;
        this.saved = true;
        initComponents();
    }

    /**
     * Creates a new instance of AbstractDataPanel.
     *
     * @param listDataModel       data model for list
     * @param statsTableDataModel data model for table with stats
     * @throws IllegalArgumentException if data model for list is null
     *                                  or data model for table with stats is null
     */
    public AbstractOverviewDataPanel(final AbstractListDataModel<T> listDataModel, final AbstractStatsTableDataModel statsTableDataModel) {
        Assert.notNull(listDataModel, "Data model for list mustn't be null.");
        Assert.notNull(statsTableDataModel, "Data model for table with stats mustn't be null.");

        this.listDataModel = listDataModel;
        this.statsTableDataModel = statsTableDataModel;
        this.saved = true;
        initComponents();
    }

    /**
     * Creates new data.
     */
    public void newData() {
        deleteData();
        listDataModel.update();
        list.clearSelection();
        list.updateUI();
        tabbedPane.removeAll();
        statsTableDataModel.update();
        statsTable.updateUI();
        saved = true;
    }

    /**
     * Clears selection.
     */
    public void clearSelection() {
        tabbedPane.removeAll();
        list.clearSelection();
    }

    /**
     * Saves.
     */
    public void save() {
        saved = true;
    }

    /**
     * Returns true if data is saved.
     *
     * @return true if data is saved
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Returns info dialog.
     *
     * @param add  true if dialog is for adding data
     * @param data data
     * @return info dialog
     */
    protected abstract AbstractInfoDialog<T> getInfoDialog(boolean add, T data);

    /**
     * Deletes data.
     */
    protected abstract void deleteData();

    /**
     * Adds data.
     *
     * @param data data
     */
    protected abstract void addData(T data);

    /**
     * Updates data.
     *
     * @param data data
     */
    protected abstract void updateData(T data);

    /**
     * Removes data.
     *
     * @param data data
     */
    protected abstract void removeData(T data);

    /**
     * Duplicates data.
     *
     * @param data data
     */
    protected abstract void duplicatesData(T data);

    /**
     * Moves up data.
     *
     * @param data data
     */
    protected abstract void moveUpData(T data);

    /**
     * Moves down data.
     *
     * @param data data
     */
    protected abstract void moveDownData(T data);

    /**
     * Returns data panel.
     *
     * @param data data
     * @return data panel
     */
    protected abstract JPanel getDataPanel(T data);

    /**
     * Updates data on change.
     *
     * @param dataPanel tabbed pane with data
     * @param data      data
     */
    protected abstract void updateDataOnChange(JTabbedPane dataPanel, T data);

    /**
     * Updates model.
     *
     * @param data data
     */
    protected void updateModel(final T data) {
        listDataModel.update();
        list.updateUI();
        getTabbedPanelDataPanel().updateData(data);
        updateState();
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        initPopupMenu(addPopupMenuItem, updatePopupMenuItem, removePopupMenuItem, duplicatePopupMenuItem, moveUpPopupMenuItem, moveDownPopupMenuItem);

        addPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
        addPopupMenuItem.addActionListener(e -> addAction());

        updatePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        updatePopupMenuItem.setEnabled(false);
        updatePopupMenuItem.addActionListener(e -> updateAction());

        removePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        removePopupMenuItem.setEnabled(false);
        removePopupMenuItem.addActionListener(e -> removeAction());

        duplicatePopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        duplicatePopupMenuItem.setEnabled(false);
        duplicatePopupMenuItem.addActionListener(e -> duplicateAction());

        moveUpPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        moveUpPopupMenuItem.setEnabled(false);
        moveUpPopupMenuItem.addActionListener(e -> moveUpAction());

        moveDownPopupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
        moveDownPopupMenuItem.setEnabled(false);
        moveDownPopupMenuItem.addActionListener(e -> moveDownAction());

        list.setModel(listDataModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setComponentPopupMenu(popupMenu);
        list.getSelectionModel().addListSelectionListener(e -> listValueChangedAction());

        initStats();

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Initializes popup menu.
     *
     * @param menuItems popup menu items
     */
    private void initPopupMenu(final JMenuItem... menuItems) {
        for (final JMenuItem menuItem : menuItems) {
            popupMenu.add(menuItem);
        }
    }

    /**
     * Initializes stats.
     */
    private void initStats() {
        if (statsTableDataModel != null) {
            statsTable.setModel(statsTableDataModel);
            statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            statsTable.setEnabled(false);
            statsTable.setRowSelectionAllowed(false);
            statsTable.setDefaultRenderer(Integer.class, new StatsTableCellRenderer());
            statsTable.setDefaultRenderer(String.class, new StatsTableCellRenderer());
        }
    }

    /**
     * Performs action for button Add.
     */
    private void addAction() {
        SwingUtilities.invokeLater(() -> {
            final AbstractInfoDialog<T> dialog = getInfoDialog(true, null);
            dialog.setVisible(true);
            if (dialog.getReturnStatus() == DialogResult.OK) {
                addData(dialog.getData());
                listDataModel.update();
                list.updateUI();
                list.setSelectedIndex(list.getModel().getSize() - 1);
                updateState();
            }
        });
    }

    /**
     * Performs action for button Update.
     */
    private void updateAction() {
        SwingUtilities.invokeLater(() -> {
            final AbstractInfoDialog<T> dialog = getInfoDialog(false, listDataModel.getObjectAt(list.getSelectedIndex()));
            dialog.setVisible(true);
            if (dialog.getReturnStatus() == DialogResult.OK) {
                final T data = dialog.getData();
                updateData(data);
                updateModel(data);
            }
        });
    }

    /**
     * Performs action for button Remove.
     */
    private void removeAction() {
        removeData(listDataModel.getObjectAt(list.getSelectedIndex()));
        listDataModel.update();
        list.updateUI();
        list.clearSelection();
        updateState();
    }

    /**
     * Performs action for button Duplicate.
     */
    private void duplicateAction() {
        final int index = list.getSelectedIndex();
        duplicatesData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        updateState();
    }

    /**
     * Performs action for button MoveUp.
     */
    private void moveUpAction() {
        final int index = list.getSelectedIndex();
        moveUpData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index - 1);
        saved = false;
        if (statsTableDataModel == null) {
            firePropertyChange(UPDATE_PROPERTY, false, true);
        }
    }

    /**
     * Performs action for button MoveDown.
     */
    private void moveDownAction() {
        final int index = list.getSelectedIndex();
        moveDownData(listDataModel.getObjectAt(index));
        listDataModel.update();
        list.updateUI();
        list.setSelectedIndex(index + 1);
        saved = false;
        if (statsTableDataModel == null) {
            firePropertyChange(UPDATE_PROPERTY, false, true);
        }
    }

    /**
     * Performs action for change of list value.
     */
    private void listValueChangedAction() {
        final boolean isSelectedRow = list.getSelectedIndices().length == 1;
        final int selectedRow = list.getSelectedIndex();
        final boolean validRowIndex = selectedRow >= 0;
        final boolean validSelection = isSelectedRow && validRowIndex;
        removePopupMenuItem.setEnabled(validSelection);
        updatePopupMenuItem.setEnabled(validSelection);
        duplicatePopupMenuItem.setEnabled(validSelection);
        tabbedPane.removeAll();
        if (validSelection) {
            final T data = listDataModel.getObjectAt(selectedRow);
            tabbedPane.add("Data", getDataPanel(data));
            updateDataOnChange(tabbedPane, data);
        }
        if (isSelectedRow && selectedRow > 0) {
            moveUpPopupMenuItem.setEnabled(true);
        } else {
            moveUpPopupMenuItem.setEnabled(false);
        }
        if (validSelection && selectedRow < list.getModel().getSize() - 1) {
            moveDownPopupMenuItem.setEnabled(true);
        } else {
            moveDownPopupMenuItem.setEnabled(false);
        }
    }

    /**
     * Updates state.
     */
    private void updateState() {
        if (statsTableDataModel == null) {
            firePropertyChange(UPDATE_PROPERTY, false, true);
        } else {
            statsTableDataModel.update();
            statsTable.updateUI();
            saved = false;
        }
    }

    /**
     * Returns data panel in tabbed pane.
     *
     * @return data panel in tabbed pane
     */
    @SuppressWarnings("unchecked")
    private AbstractDataPanel<T> getTabbedPanelDataPanel() {
        return (AbstractDataPanel<T>) tabbedPane.getComponentAt(0);
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group data = layout.createSequentialGroup()
            .addComponent(listScrollPane, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE)
            .addGap(HORIZONTAL_GAP_SIZE)
            .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        if (statsTableDataModel == null) {
            return data;
        } else {
            return layout.createParallelGroup()
                .addGroup(data)
                .addComponent(statsTableScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        }
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group data = layout.createParallelGroup()
            .addComponent(listScrollPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE)
            .addComponent(tabbedPane, VERTICAL_DATA_COMPONENT_SIZE, VERTICAL_DATA_COMPONENT_SIZE, Short.MAX_VALUE);

        if (statsTableDataModel == null) {
            return data;
        } else {
            return layout.createSequentialGroup()
                .addGroup(data)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(statsTableScrollPane, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE, VERTICAL_STATS_SCROLL_PANE_SIZE);
        }
    }

}
