package net.thumbtack.school.database.jdbc;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcService {

    public static void insertTrainee(Trainee trainee) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String insertQuery = "insert into trainee values(?,?,?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, trainee.getFirstName());
            stmt.setString(3, trainee.getLastName());
            stmt.setInt(4, trainee.getRating());
            stmt.setNull(5, Types.INTEGER);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trainee.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    public static void updateTrainee(Trainee trainee) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "update trainee set firstname=?, lastname=?, rating=? where id=?";
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            stmt.setString(1, trainee.getFirstName());
            stmt.setString(2, trainee.getLastName());
            stmt.setInt(3, trainee.getRating());
            stmt.setInt(4, trainee.getId());
            stmt.executeUpdate();
        }
    }

    public static Trainee getTraineeByIdUsingColNames(int traineeId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "select * from trainee where id=?";
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            stmt.setInt(1, traineeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                int rating = rs.getInt("rating");
                return new Trainee(id, firstName, lastName, rating);
            }
            return null;
        }
    }

    public static Trainee getTraineeByIdUsingColNumbers(int traineeId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "select * from trainee where id=?";
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            stmt.setInt(1, traineeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                int rating = rs.getInt(4);
                return new Trainee(id, firstName, lastName, rating);
            }
            return null;
        }
    }

    public static List<Trainee> getTraineesUsingColNames() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "select * from trainee";
        List<Trainee> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                int rating = rs.getInt("rating");
                list.add(new Trainee(id, firstName, lastName, rating));
            }
            return list;
        }
    }

    public static List<Trainee> getTraineesUsingColNumbers() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "select * from trainee";
        List<Trainee> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                int rating = rs.getInt(4);
                list.add(new Trainee(id, firstName, lastName, rating));
            }
            return list;
        }
    }

    public static void deleteTrainee(Trainee trainee) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "delete from trainee where id=?";
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            stmt.setInt(1, trainee.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteTrainees() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String updateTraineeQuery = "delete from trainee";
        try (PreparedStatement stmt = con.prepareStatement(updateTraineeQuery)) {
            stmt.executeUpdate();
        }
    }

    public static void insertSubject(Subject subject) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String insertSubject = "insert into subject values(?,?)";
        try (PreparedStatement stmt = con.prepareStatement(insertSubject, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, subject.getSubjectName());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    subject.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating subject failed, no ID obtained.");
                }
            }

        }
    }

    public static Subject getSubjectByIdUsingColNames(int subjectId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSubject = "select * from subject where id=?";
        try (PreparedStatement stmt = con.prepareStatement(getSubject)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("subjectname");
                return new Subject(id, name);
            }
            return null;
        }
    }

    public static Subject getSubjectByIdUsingColNumbers(int subjectId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSubject = "select * from subject where id=?";
        try (PreparedStatement stmt = con.prepareStatement(getSubject)) {
            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                return new Subject(id, name);
            }
            return null;
        }
    }

    public static void deleteSubjects() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String deleteSubjects = "delete from subject";
        try (PreparedStatement stmt = con.prepareStatement(deleteSubjects)) {
            stmt.executeUpdate();
        }
    }

    public static void insertSchool(School school) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String insertSchool = "insert into school values(?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(insertSchool, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, school.getSchoolName());
            stmt.setInt(3, school.getYear());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    school.setId(id);
                } else {
                    throw new SQLException("Creating school failed, no ID obtained.");
                }
            }
        }
    }

    public static School getSchoolByIdUsingColNames(int schoolId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSchoolById = "select * from school where id=?";
        try (PreparedStatement stmt = con.prepareStatement(getSchoolById)) {
            stmt.setInt(1, schoolId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String schoolName = rs.getString("schoolName");
                int year = rs.getInt("year");
                return new School(id, schoolName, year);
            }
            return null;
        }
    }

    public static School getSchoolByIdUsingColNumbers(int schoolId) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSchoolById = "select * from school where id=?";
        try (PreparedStatement stmt = con.prepareStatement(getSchoolById)) {
            stmt.setInt(1, schoolId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String schoolName = rs.getString(2);
                int year = rs.getInt(3);
                return new School(id, schoolName, year);
            }
            return null;
        }
    }

    public static void deleteSchools() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String deleteSchools = "delete from school";
        try (PreparedStatement stmt = con.prepareStatement(deleteSchools)) {
            stmt.executeUpdate();
        }
    }

    public static void insertGroup(School school, Group group) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String deleteSchools = "insert into groups values(?,?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(deleteSchools, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setInt(2, school.getId());
            stmt.setString(3, group.getGroupName());
            stmt.setString(4, group.getRoomName());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    group.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Can not insert group, no ID obtained");
                }
            }
        }
    }

    public static School getSchoolByIdWithGroups(int id) throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSchoolByIDWithGroups = "select schoolName, year, groupname, roomname, groups.id from school, groups WHERE schoolid = school.id and schoolid=?";
        try (PreparedStatement stmt = con.prepareStatement(getSchoolByIDWithGroups)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            School school = null;
            while (rs.next()) {
                String name = rs.getString("schoolname");
                int year = rs.getInt("year");
                int groupId = rs.getInt("id");
                String groupName = rs.getString("groupname");
                String roomName = rs.getString("roomname");
                if (school == null) {
                    school = new School(id, name, year);
                }
                school.addGroup(new Group(groupId, groupName, roomName));
            }
            return school;
        }
    }

    public static List<School> getSchoolsWithGroups() throws SQLException {
        Connection con = JdbcUtils.getConnection();
        String getSchoolsWithGroups = "select school.id as schoolid, schoolName, year, groups.id as groupsid, groupname, roomname from school, groups WHERE schoolid = school.id";
        List<School> schools = new ArrayList<>();
        School school = null;
        try (PreparedStatement stmt = con.prepareStatement(getSchoolsWithGroups)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int schoolId = rs.getInt("schoolid");
                String schoolName = rs.getString("schoolname");
                int year = rs.getInt("year");
                int groupId = rs.getInt("groupsid");
                String groupName = rs.getString("groupname");
                String roomName = rs.getString("roomname");
                if (school != null && school.getId() != schoolId) {
                    schools.add(school);
                    school = null;
                }
                if (school == null) {
                    school = new School(schoolId, schoolName, year);
                }
                school.addGroup(new Group(groupId, groupName, roomName));
            }
            if (school != null)
                schools.add(school);
        }
        return schools;
    }
}
