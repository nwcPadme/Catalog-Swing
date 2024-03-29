package cz.vhromada.catalog.gui.common;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import cz.vhromada.common.Time;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents panel for data about time.
 *
 * @author Vladimir Hromada
 */
public final class TimeDataPanel extends JPanel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal label size
     */
    private static final int HORIZONTAL_LABEL_SIZE = 100;

    /**
     * Horizontal time size
     */
    private static final int HORIZONTAL_TIME_SIZE = 60;

    /**
     * Horizontal gap size
     */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /**
     * Maximum hours
     */
    private static final int MAX_HOURS = 23;

    /**
     * Maximum minutes
     */
    private static final int MAX_MINUTES = 59;

    /**
     * Maximum seconds
     */
    private static final int MAX_SECONDS = 59;

    /**
     * Label for length
     */
    private final JLabel lengthLabel = new JLabel();

    /**
     * Spinner for length - hours
     */
    private final JSpinner lengthHoursData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_HOURS, 1));

    /**
     * Spinner for length - minutes
     */
    private final JSpinner lengthMinutesData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_MINUTES, 1));

    /**
     * Spinner for length - seconds
     */
    private final JSpinner lengthSecondsData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_SECONDS, 1));

    /**
     * Creates a new instance of TimeDataPanel.
     *
     * @param labelText text of label
     * @throws IllegalArgumentException if text of label is null
     *                                  or text of label is empty string
     */
    public TimeDataPanel(final String labelText) {
        Assert.notNull(labelText, "Label text mustn't be null.");
        Assert.isTrue(!StringUtils.isEmpty(labelText) && !StringUtils.isEmpty(labelText.trim()), "Label text mustn't be empty string.");

        this.lengthLabel.setFocusable(false);
        this.lengthLabel.setText(labelText);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Returns length.
     *
     * @return length
     */
    public Time getLength() {
        final int hours = (Integer) lengthHoursData.getValue();
        final int minutes = (Integer) lengthMinutesData.getValue();
        final int seconds = (Integer) lengthSecondsData.getValue();

        return new Time(hours, minutes, seconds);
    }

    /**
     * Sets length.
     *
     * @param length length
     * @throws IllegalArgumentException if length is null
     */
    public void setLength(final Time length) {
        Assert.notNull(length, "Length mustn't be null.");

        lengthHoursData.setValue(length.getData(Time.TimeData.HOUR));
        lengthMinutesData.setValue(length.getData(Time.TimeData.MINUTE));
        lengthSecondsData.setValue(length.getData(Time.TimeData.SECOND));
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        return layout.createSequentialGroup()
            .addComponent(lengthLabel, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
            .addGap(HORIZONTAL_GAP_SIZE)
            .addComponent(lengthHoursData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
            .addGap(HORIZONTAL_GAP_SIZE)
            .addComponent(lengthMinutesData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
            .addGap(HORIZONTAL_GAP_SIZE)
            .addComponent(lengthSecondsData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        return layout.createParallelGroup()
            .addComponent(lengthLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
            .addComponent(lengthHoursData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
            .addComponent(lengthMinutesData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
            .addComponent(lengthSecondsData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
    }

}
