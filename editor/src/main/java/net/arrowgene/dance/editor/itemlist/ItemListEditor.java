/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
 * Copyright (C) 2013-2018  Daniel Neuendorf
 *
 * Github: https://github.com/Arrowgene/net.arrowgene.dance
 * Web: https://arrowgene.net
 *
 * net.arrowgene.dance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * net.arrowgene.dance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.arrowgene.dance.editor.itemlist;

import net.arrowgene.dance.database.sqlite.SQLiteDb;
import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.library.common.FileOp;
import net.arrowgene.dance.library.file.Iteminfo;
import net.arrowgene.dance.library.models.item.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemListEditor extends EditorFrame implements ActionListener, ListSelectionListener {

    private JPanel panelMain;
    private JList listItems;
    private JButton buttonOpenFile;
    private JLabel labelPath;
    private JLabel labelHeadA;
    private JLabel labelHeadB;
    private JLabel labelHeadItems;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private JTextField textFieldModelId;
    private JTextField textFieldMinLevel;
    private JComboBox comboBoxDuration;
    private JComboBox comboBoxCategory;
    private JComboBox comboBoxSex;
    private JComboBox comboBoxPriceCategory;
    private JComboBox comboBoxQuantity;
    private JButton buttonNewItem;
    private JButton buttonSaveItem;
    private JButton buttonSaveFile;
    private JCheckBox checkBoxWedding;
    private JButton buttonImportToDb;
    private Iteminfo iteminfo;

    public ItemListEditor(EditorConfig config) {
        super(config, "Item List Editor");
        iteminfo = new Iteminfo();
    }

    @Override
    public void menuClicked() {

    }

    @Override
    public void init() {

        this.pack();
        this.setMinimumSize(new Dimension(600, 400));
        this.setSize(new Dimension(800, 800));
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setLocation(0, 0);

        //  this.panelMain = new JPanel();
        this.add(panelMain);

        //  this.listItems = new JList<>();
        this.listItems.addListSelectionListener(this);

        //  this.buttonOpenFile = new JButton();
        this.buttonOpenFile.setActionCommand("open-file");
        this.buttonOpenFile.addActionListener(this);

        //  this.buttonSaveFile = new JButton();
        this.buttonSaveFile.setActionCommand("save-file");
        this.buttonSaveFile.addActionListener(this);

        //  this.buttonNewItem = new JButton();
        this.buttonNewItem.setActionCommand("new-item");
        this.buttonNewItem.addActionListener(this);

        //  this.buttonSaveItem = new JButton();
        this.buttonSaveItem.setActionCommand("save-item");
        this.buttonSaveItem.addActionListener(this);

        //  this.buttonImportToDb = new JButton();
        this.buttonImportToDb.setActionCommand("import-to-db");
        this.buttonImportToDb.addActionListener(this);

        //  this.comboBoxSex = new JComboBox<>();
        this.comboBoxSex.setModel(new DefaultComboBoxModel<>(ItemSexType.values()));

        //   this.comboBoxCategory = new JComboBox<>();
        this.comboBoxCategory.setModel(new DefaultComboBoxModel<>(ItemCategoryType.values()));

        //    this.comboBoxDuration = new JComboBox<>();
        this.comboBoxDuration.setModel(new DefaultComboBoxModel<>(ItemDurationType.values()));

        //    this.comboBoxPriceCategory = new JComboBox<>();
        this.comboBoxPriceCategory.setModel(new DefaultComboBoxModel<>(ItemPriceCategoryType.values()));

        //    this.comboBoxQuantity = new JComboBox<>();
        this.comboBoxQuantity.setModel(new DefaultComboBoxModel<>(ItemQuantityType.values()));

        super.init();
    }

    @Override
    public String getMenuCategory() {
        return "Tools";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("open-file")) {
            this.openFile();
        } else if (e.getActionCommand().equals("save-file")) {
            this.saveFile();
        } else if (e.getActionCommand().equals("new-item")) {
            this.newItem();
        } else if (e.getActionCommand().equals("save-item")) {
            this.saveFile();
        } else if (e.getActionCommand().equals("import-to-db")) {
            this.importToDb();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            ShopItem selected = (ShopItem) this.listItems.getSelectedValue();
            if (selected != null) {
                this.textFieldName.setText(selected.getName());
                this.textFieldId.setText(Integer.toString(selected.getId()));
                this.textFieldMinLevel.setText(Integer.toString(selected.getMinLevel()));
                this.textFieldModelId.setText(Integer.toString(selected.getModelId()));
                this.textFieldPrice.setText(Integer.toString(selected.getPrice()));

                this.comboBoxSex.setSelectedItem(selected.getSex());
                this.comboBoxQuantity.setSelectedItem(selected.getQuantity());
                this.comboBoxPriceCategory.setSelectedItem(selected.getPriceCategory());
                this.comboBoxDuration.setSelectedItem(selected.getDuration());
                this.comboBoxCategory.setSelectedItem(selected.getCategory());

                this.checkBoxWedding.setSelected(selected.isWeddingRing());
            }
        }
    }

    private void importToDb() {
        SQLiteDb db = new SQLiteDb();
        List<ShopItem> items = iteminfo.getItems();
        db.insertShopItems(items);
    }

    private void newItem() {

    }

    private void saveItem() {

    }

    private void saveFile() {

    }

    private void openFile() {
        String[] extensions = {"dat"};
        File f = FileOp.chooseFile("Item List", extensions, this.config.getDancePath(), this);
        if (f != null) {
            try {
                this.iteminfo = new Iteminfo(f.getPath());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            this.labelPath.setText(f.getPath());
            this.labelHeadA.setText(Integer.toString(iteminfo.getHeadA()));
            this.labelHeadB.setText(Integer.toString(iteminfo.getHeadB()));
            this.labelHeadItems.setText(Integer.toString(iteminfo.getHeadItemCount()));
            System.out.println("Opened: " + f.getName());
            this.reloadItemList();
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void reloadItemList() {
        List<ShopItem> items = iteminfo.getItems();
        DefaultListModel<ShopItem> itemListModel = new DefaultListModel<>();
        for (ShopItem item : items) {
            itemListModel.addElement(item);
        }
        this.listItems.setModel(itemListModel);
    }

}
