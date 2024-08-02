package students;

import dbConnection.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Polash
 */
public class Course {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    //get table max row
    public int getMaxId() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from course");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    public boolean getId(int id) {
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.studentID1.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Student id doesn't exist");
            }

        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int countSemester(int id) {
        int total = 0;
        try {
            ps = con.prepareStatement("select count(*) as 'total' from course where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt(1);
            }
            if (total == 8) {
                JOptionPane.showMessageDialog(null, "This student has completed all the courses");
                return -1;
            }
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;

    }

    //get all the student courses value from MySQL course table
    public void getStudentsCourses(JTable table, String valueToSearch) {
        String sql = "SELECT * FROM course WHERE CONCAT (student_id,semester)LIKE ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + valueToSearch + "%");

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[8];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Check whether the student has already taken this course or not
    public boolean isCourseExist(int id, String courseNo, String course) {
        boolean isExist = false;
        String sql = "SELECT * FROM course WHERE student_id = ? and " + courseNo + " = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, course);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    //Check whether the student has already taken this semester or not
    public boolean isSemesterExist(int sid, int semesterNum) {
        boolean isExist = false;
        try {
            ps = con.prepareStatement("SELECT * FROM course WHERE student_id = ? and semester = ?");
            ps.setInt(1, sid);
            ps.setInt(2,  semesterNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isExist = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    //insert data into course table
    public void insert(int id, int sId,int semester, String course1, String course2, String course3, String course4, String course5) {
        String sql = "INSERT INTO course VALUES (?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, sId);
            ps.setInt(3, semester);
            ps.setString(4, course1);
            ps.setString(5, course2);
            ps.setString(6, course3);
            ps.setString(7, course4);
            ps.setString(8, course5);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Courses added successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
