import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableExample extends JFrame {
    private DefaultTableModel model;

    public TableExample() {
        // ... (โค้ดส่วนที่เหมือนเดิม)
        JFrame frame = new JFrame("ตัวอย่างตาราง");

        // ขนาดของ Frame
        frame.setSize(600, 400);

        // ปิดโปรแกรมเมื่อปิดหน้าต่าง
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // สร้าง DefaultTableModel
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "First Name", "Last Name"});

        // สร้างตาราง
        JTable table = new JTable(model);

        // ... (โค้ดส่วนที่เหมือนเดิม)

        // เพิ่ม JButton เพื่อเพิ่มข้อมูล
        JButton addButton = new JButton("Add Row");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // เพิ่มแถวใหม่ในตาราง
                model.addRow(new Object[]{"4", "New", "Row"});
            }
        });

        // เพิ่ม JButton เพื่อลบแถวที่เลือก
        JButton removeButton = new JButton("Remove Selected Row");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ลบแถวที่เลือก
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                }
            }
        });

        // เพิ่ม JButton เพื่ออัปเดตข้อมูล
        JButton updateButton = new JButton("Update Selected Row");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // อัปเดตข้อมูลในแถวที่เลือก
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    System.out.println(model.getValueAt( selectedRow, 1));
                }
            }
        });

        // เพิ่ม JButton ลงใน Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);

        // เพิ่ม Panel ลงใน Frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(table);

        // เพิ่ม JScrollPane ลงใน Frame
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // แสดง Frame
        frame.setVisible(true);
        // ... (โค้ดส่วนที่เหมือนเดิม)

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TableExample();
        });
    }
    // ... (โค้ดส่วนที่เหมือนเดิม)

}
