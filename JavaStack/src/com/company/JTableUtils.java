package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class JTableUtils {

    public static final int DEFAULT_GAP = 6;
    public static final int DEFAULT_COLUMN_WIDTH = 40;
    public static final int DEFAULT_ROW_HEADER_WIDTH = 40;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final char DELETE_KEY_CHAR_CODE = 127;
    private static final Border DEFAULT_CELL_BORDER = BorderFactory.createEmptyBorder(0, 3, 0, 3);
    private static final Border DEFAULT_RENDERER_CELL_BORDER = DEFAULT_CELL_BORDER;
    private static final Border DEFAULT_EDITOR_CELL_BORDER = BorderFactory.createEmptyBorder(0, 3, 0, 2);
    private static final Map<JTable, Integer> tableColumnWidths = new HashMap<>();

    private static <T extends JComponent> T setFixedSize(T comp, int width, int height) {
        Dimension d = new Dimension(width, height);
        comp.setMaximumSize(d);
        comp.setMinimumSize(d);
        comp.setPreferredSize(d);
        comp.setSize(d);
        return comp;
    }

    private static JButton createPlusMinusButton(String text, int size) {
        JButton button = new JButton(text);
        setFixedSize(button, size, size).setMargin(new Insets(0, 0, 0, 0));
        button.setFocusable(false);
        button.setFocusPainted(false);
        return button;
    }

    private static int getColumnWidth(JTable table) {
        Integer columnWidth = tableColumnWidths.get(table);
        if (columnWidth == null) {
            if (table.getColumnCount() > 0) {
                columnWidth = table.getWidth() / table.getColumnCount();
            } else {
                columnWidth = DEFAULT_COLUMN_WIDTH;
            }
        }
        return columnWidth;
    }

    private static void recalcJTableSize(JTable table) {
        int width = getColumnWidth(table) * table.getColumnCount();
        int height = 0, rowCount = table.getRowCount();
        for (int r = 0; r < rowCount; r++)
            height += table.getRowHeight(height);
        setFixedSize(table, width, height);

        if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            if (scrollPane.getRowHeader() != null) {
                Component rowHeaderView = scrollPane.getRowHeader().getView();
                if (rowHeaderView instanceof JList) {
                    ((JList) rowHeaderView).setFixedCellHeight(table.getRowHeight());
                }
                scrollPane.getRowHeader().repaint();
            }
        }
    }

    private static void addRowHeader(JTable table, TableModel tableModel, JScrollPane scrollPane) {
        final class RowHeaderRenderer extends JLabel implements ListCellRenderer {
            RowHeaderRenderer() {
                JTableHeader header = table.getTableHeader();
                setOpaque(true);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setHorizontalAlignment(CENTER);
                setForeground(header.getForeground());
                setBackground(header.getBackground());
                setFont(header.getFont());
            }

            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected, boolean cellHasFocus) {
                setText(String.format("[%d]", index));
                return this;
            }
        }

        ListModel lm = new AbstractListModel() {
            @Override
            public int getSize() {
                return tableModel.getRowCount();
            }

            @Override
            public Object getElementAt(int index) {
                return String.format("[%d]", index);
            }
        };

        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(DEFAULT_ROW_HEADER_WIDTH);
        rowHeader.setFixedCellHeight(
                table.getRowHeight()// + table.getRowMargin()// + table.getIntercellSpacing().height
        );
        rowHeader.setCellRenderer(new RowHeaderRenderer());

        scrollPane.setRowHeaderView(rowHeader);
        scrollPane.getRowHeader().getView().setBackground(scrollPane.getColumnHeader().getBackground());
    }

    public static void initJTableForArray(
            JTable table, int defaultColWidth,
            boolean showRowsIndexes, boolean showColsIndexes,
            boolean changeRowsCountButtons, boolean changeColsCountButtons,
            int changeButtonsSize, int changeButtonsMargin
    ) {
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        if (!showColsIndexes && table.getTableHeader() != null) {
            table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        }
        table.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(false);
        table.setDragEnabled(false);
        table.putClientProperty("terminateEditOnFocusLost", true);

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"[0]"}, 1) {
            @Override
            public String getColumnName(int index) {
                return String.format("[%d]", index);
            }
        };
        table.setModel(tableModel);
        tableColumnWidths.put(table, defaultColWidth);
        recalcJTableSize(table);

        if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            if (changeRowsCountButtons || changeColsCountButtons) {
                List<Component> linkedComponents = new ArrayList<>();

                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                BorderLayout borderLayout = new BorderLayout(changeButtonsMargin, changeButtonsMargin);
                FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);

                JPanel panel = new JPanel(borderLayout);
                panel.setBackground(TRANSPARENT);

                if (changeColsCountButtons) {
                    JPanel topPanel = new JPanel(flowLayout);
                    topPanel.setBackground(TRANSPARENT);
                    if (changeRowsCountButtons) {
                        topPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsSize + changeButtonsMargin, changeButtonsSize));
                    }
                    JButton minusButton = createPlusMinusButton("\u2013", changeButtonsSize);
                    minusButton.setName(table.getName() + "-minusColumnButton");
                    minusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.setColumnCount(tableModel.getColumnCount() > 0 ? tableModel.getColumnCount() - 1 : 0);
                        recalcJTableSize(table);
                    });
                    topPanel.add(minusButton);
                    linkedComponents.add(minusButton);
                    topPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsMargin, changeButtonsSize));
                    JButton plusButton = createPlusMinusButton("+", changeButtonsSize);
                    plusButton.setName(table.getName() + "-plusColumnButton");
                    plusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.addColumn(String.format("[%d]", tableModel.getColumnCount()));
                        recalcJTableSize(table);
                    });
                    topPanel.add(plusButton);
                    linkedComponents.add(plusButton);

                    panel.add(topPanel, BorderLayout.NORTH);
                }
                if (changeRowsCountButtons) {
                    JPanel leftPanel = setFixedSize(new JPanel(flowLayout), changeButtonsSize, changeButtonsSize);
                    leftPanel.setBackground(TRANSPARENT);
                    JButton minusButton = createPlusMinusButton("\u2013", changeButtonsSize);
                    minusButton.setName(table.getName() + "-minusRowButton");
                    minusButton.addActionListener((ActionEvent evt) -> {
                        if (tableModel.getRowCount() > 0) {
                            tableModel.removeRow(tableModel.getRowCount() - 1);
                            recalcJTableSize(table);
                        }
                    });
                    leftPanel.add(minusButton);
                    linkedComponents.add(minusButton);
                    leftPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsSize, changeButtonsMargin));
                    JButton plusButton = createPlusMinusButton("+", changeButtonsSize);
                    plusButton.setName(table.getName() + "-plusRowButton");
                    plusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.setRowCount(tableModel.getRowCount() + 1);
                        recalcJTableSize(table);
                    });
                    leftPanel.add(plusButton);
                    linkedComponents.add(plusButton);

                    panel.add(leftPanel, BorderLayout.WEST);
                }
                table.setPreferredScrollableViewportSize(null);
                JScrollPane newScrollPane = new JScrollPane(table);
                newScrollPane.setBackground(scrollPane.getBackground());
                newScrollPane.setBorder(scrollPane.getBorder());
                scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                panel.add(newScrollPane, BorderLayout.CENTER);

                scrollPane.getViewport().removeAll();
                scrollPane.add(panel);
                scrollPane.getViewport().add(panel);

                table.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                    if ("enabled".equals(evt.getPropertyName())) {
                        boolean enabled = (boolean) evt.getNewValue();
                        linkedComponents.forEach((comp) -> comp.setEnabled(enabled));
                        if (!enabled) {
                            table.clearSelection();
                        }
                    }
                });
                linkedComponents.forEach((comp) -> comp.setEnabled(table.isEnabled()));

                scrollPane.setVisible(false);
                scrollPane.setVisible(true);

                scrollPane = newScrollPane;
            }

            table.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if ("enabled".equals(evt.getPropertyName())) {
                    boolean enabled = (boolean) evt.getNewValue();
                    if (!enabled) {
                        table.clearSelection();
                    }
                } else if ("rowHeight".equals(evt.getPropertyName())) {
                    recalcJTableSize(table);
                }
            });


            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    if (evt.getKeyChar() == DELETE_KEY_CHAR_CODE) {
                        for (int r : table.getSelectedRows()) {
                            for (int c : table.getSelectedColumns()) {
                                table.setValueAt(null, r, c);
                            }
                        }
                    }
                }
            });

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        label.setHorizontalAlignment((value == null || value.toString().matches("|-?\\d+")) ? RIGHT : LEFT);
                        label.setBorder(DEFAULT_RENDERER_CELL_BORDER);
                    }
                    return comp;
                }
            });

            table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                    if (comp instanceof JTextField) {
                        JTextField textField = (JTextField) comp;
                        textField.setHorizontalAlignment((value == null || value.toString().matches("|-?\\d+")) ? SwingConstants.RIGHT : SwingConstants.LEFT);
                        textField.setBorder(DEFAULT_EDITOR_CELL_BORDER);
                        textField.selectAll();  // чтобы при начале печати перезаписывать текст
                    }
                    return comp;
                }
            });

            if (showRowsIndexes) {
                addRowHeader(table, tableModel, scrollPane);
            }
        }
    }

    public static void initJTableForArray(
            JTable table, int defaultColWidth,
            boolean showRowsIndexes, boolean showColsIndexes,
            boolean changeRowsCountButtons, boolean changeColsCountButtons
    ) {
        initJTableForArray(
                table, defaultColWidth,
                showRowsIndexes, showColsIndexes, changeRowsCountButtons, changeColsCountButtons,
                22, DEFAULT_GAP
        );
    }

    private static void writeArrayToJTable(JTable table, Object array, String itemFormat) {
        if (!array.getClass().isArray()) {
            return;
        }
        if (!(table.getModel() instanceof DefaultTableModel)) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableColumnWidths.put(table, getColumnWidth(table));

        if (itemFormat == null || itemFormat.length() == 0) {
            itemFormat = "%s";
        }
        int rank = 1;
        int len1 = Array.getLength(array), len2 = -1;
        if (len1 > 0) {
            for (int i = 0; i < len1; i++) {
                Object item = Array.get(array, i);
                if (item != null && item.getClass().isArray()) {
                    rank = 2;
                    len2 = Math.max(Array.getLength(item), len2);
                }
            }
        }
        tableModel.setRowCount(rank == 1 ? 1 : len1);
        tableModel.setColumnCount(rank == 1 ? len1 : len2);
        for (int i = 0; i < len1; i++) {
            if (rank == 1) {
                tableModel.setValueAt(String.format(itemFormat, Array.get(array, i)), 0, i);
            } else {
                Object line = Array.get(array, i);
                if (line != null) {
                    if (line.getClass().isArray()) {
                        int lineLen = Array.getLength(line);
                        for (int j = 0; j < lineLen; j++) {
                            tableModel.setValueAt(String.format(itemFormat, Array.get(line, j)), i, j);
                        }
                    } else {
                        tableModel.setValueAt(String.format(itemFormat, Array.get(array, i)), 0, i);
                    }
                }
            }
        }
        recalcJTableSize(table);
    }

    public static void writeArrayToJTable(JTable table, String[] array) {
        writeArrayToJTable(table, array, "%s");
    }
}
