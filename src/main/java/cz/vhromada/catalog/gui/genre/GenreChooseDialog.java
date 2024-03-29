package cz.vhromada.catalog.gui.genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.gui.common.CatalogSwingConstants;
import cz.vhromada.catalog.gui.common.DialogResult;
import cz.vhromada.catalog.gui.common.Picture;

import org.springframework.util.Assert;

/**
 * A class represents dialog for choosing genre.
 *
 * @author Vladimir Hromada
 */
public final class GenreChooseDialog extends JDialog {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal scroll pane size
     */
    private static final int HORIZONTAL_SCROLL_PANE_SIZE = 310;

    /**
     * Horizontal button size
     */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /**
     * Horizontal button gap size
     */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /**
     * Horizontal size of gap between button
     */
    private static final int HORIZONTAL_BUTTONS_GAP_SIZE = 54;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /**
     * Vertical scroll pane size
     */
    private static final int VERTICAL_SCROLL_PANE_SIZE = 300;

    /**
     * Vertical gap size
     */
    private static final int VERTICAL_GAP_SIZE = 20;

    /**
     * Return status
     */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /**
     * Facade for genres
     */
    private final GenreFacade genreFacade;

    /**
     * List of genres
     */
    private List<Genre> genres;

    /**
     * List with genres
     */
    private final JList<String> list = new JList<>();

    /**
     * ScrollPane for list with genres
     */
    private final JScrollPane listScrollPane = new JScrollPane(list);

    /**
     * Button OK
     */
    private final JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /**
     * Button Cancel
     */
    private final JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /**
     * Data model for list for genres
     */
    private GenresListDataModel genreListDataModel;

    /**
     * Creates a new instance of GenreChooseDialog.
     *
     * @param genreFacade facade for genres
     * @param genres      list of genres
     * @throws IllegalArgumentException if facade for genres is null
     *                                  or list of genres is null
     */
    public GenreChooseDialog(final GenreFacade genreFacade, final List<Genre> genres) {
        super(new JFrame(), "Choose", true);

        Assert.notNull(genreFacade, "Facade for genres mustn't be null.");
        Assert.notNull(genres, "List of genres mustn't be null.");

        this.genreFacade = genreFacade;
        this.genres = new ArrayList<>(genres);
        initComponents();
        setIconImage(Picture.CHOOSE.getIcon().getImage());
    }

    /**
     * Returns return status.
     *
     * @return return status
     */
    public DialogResult getReturnStatus() {
        return returnStatus;
    }

    /**
     * Returns list of genres.
     *
     * @return list of genres
     * @throws IllegalStateException if list of genres for hasn't been set
     */
    public List<Genre> getGenres() {
        Assert.state(genres != null, "List of genres mustn't be null.");

        return Collections.unmodifiableList(genres);
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        genreListDataModel = new GenresListDataModel(genreFacade);
        list.setModel(genreListDataModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndices(getSelectedIndexes());
        list.addListSelectionListener(e -> selectionChangeAction());

        okButton.setEnabled(list.getSelectedIndices().length > 0);
        okButton.addActionListener(e -> okAction());

        cancelButton.addActionListener(e -> cancelAction());

        final GroupLayout layout = new GroupLayout(getRootPane());
        getRootPane().setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));

        pack();
        setLocationRelativeTo(getRootPane());
    }

    /**
     * Performs action for button OK.
     */
    private void okAction() {
        returnStatus = DialogResult.OK;
        genres.clear();
        final int[] indexes = list.getSelectedIndices();
        for (final int index : indexes) {
            genres.add(genreListDataModel.getObjectAt(index));
        }
        close();
    }

    /**
     * Performs action for button Cancel.
     */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        genres = null;
        close();
    }

    /**
     * Performs action for selection change.
     */
    private void selectionChangeAction() {
        final int[] indexes = list.getSelectedIndices();
        okButton.setEnabled(indexes.length > 0);
    }

    /**
     * Closes dialog.
     */
    private void close() {
        setVisible(false);
        dispose();
    }

    /**
     * Returns selected indexes.
     *
     * @return selected indexes
     */
    private int[] getSelectedIndexes() {
        final List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genreListDataModel.getSize(); i++) {
            if (genres.contains(genreListDataModel.getObjectAt(i))) {
                indexes.add(i);
            }
        }

        final int[] result = new int[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            result[i] = indexes.get(i);
        }

        return result;
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
            .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
            .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
            .addGap(HORIZONTAL_BUTTONS_GAP_SIZE)
            .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
            .addGap(HORIZONTAL_BUTTON_GAP_SIZE);


        final GroupLayout.Group components = layout.createParallelGroup()
            .addComponent(listScrollPane, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE, HORIZONTAL_SCROLL_PANE_SIZE)
            .addGroup(buttons);

        return layout.createSequentialGroup()
            .addGap(HORIZONTAL_GAP_SIZE)
            .addGroup(components)
            .addGap(HORIZONTAL_GAP_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
            .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
            .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
            .addGap(VERTICAL_GAP_SIZE)
            .addComponent(listScrollPane, VERTICAL_SCROLL_PANE_SIZE, VERTICAL_SCROLL_PANE_SIZE, VERTICAL_SCROLL_PANE_SIZE)
            .addGap(VERTICAL_GAP_SIZE)
            .addGroup(buttons)
            .addGap(VERTICAL_GAP_SIZE);
    }

}
