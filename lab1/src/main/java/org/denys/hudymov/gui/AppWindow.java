package org.denys.hudymov.gui;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.denys.hudymov.entity.Client;
import org.denys.hudymov.repository.ClientDao;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppWindow extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton button1;
    private JButton button3;
    private JButton button4;
    private JPanel crudPanel;
    private JTable clientsTable;
    private JButton button2;
    private ClientDao clientDao = ClientDao.builder().build();

    public AppWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setContentPane(getPanel1());
        addColumns();
        render();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void addColumns() {
        String[] columns = {"Client ID", "Surname", "Name", "Patronymic", "Passport", "Comment"};
        List<Client> clients = clientDao.read();
        DefaultTableModel columnModel = new DefaultTableModel();
        columnModel.setColumnIdentifiers(columns);
        clients.forEach(client -> {
            Vector<Object> row = new Vector<>();
            row.add(client.getClientId());
            row.add(client.getSurname());
            row.add(client.getName());
            row.add(client.getPatronymic());
            row.add(client.getPassportData());
            row.add(client.getComment());
            columnModel.addRow(row);
        });
        getClientsTable().setModel(columnModel);
    }

    private void render() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        getClientsTable().setDefaultRenderer(String.class, centerRenderer);
    }
}
