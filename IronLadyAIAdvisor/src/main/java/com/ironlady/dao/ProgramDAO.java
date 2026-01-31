package com.ironlady.dao;

import com.ironlady.model.Program;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramDAO {

	private final String URL = "jdbc:mysql://localhost:3306/ironlady";
	private final String USER = "root";
	private final String PASSWORD = "coader"; // change if needed

	private Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	// ✅ Get all programs
	public List<Program> getAllPrograms() {
		List<Program> programs = new ArrayList<>();
		String query = "SELECT * FROM programs";

		try (Connection con = getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Program p = new Program();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setTarget(rs.getString("target"));
				p.setInterest(rs.getString("interest"));
				p.setDuration(rs.getString("duration")); // ✅ int
				p.setOutcome(rs.getString("outcome"));

				programs.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return programs;
	}

	// ✅ Get program by target
	public Program getProgramByTarget(String target) {
		Program program = null;
		String query = "SELECT * FROM programs WHERE target = ?";

		try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, target);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				program = new Program();
				program.setId(rs.getInt("id"));
				program.setName(rs.getString("name"));
				program.setTarget(rs.getString("target"));
				program.setInterest(rs.getString("interest"));
				program.setDuration(rs.getString("duration")); // ✅ int
				program.setOutcome(rs.getString("outcome"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return program;
	}
}
