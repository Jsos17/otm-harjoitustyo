package calculatorapp.dao;

import calculatorapp.logic.Expression;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is the data access object for the Expression table. It supports
 * insertion, deletion, search and retrieval operations as well as counting the
 * expressions that exist in the database.
 *
 * @author jpssilve
 */
public class ExpressionDao implements MathDao {

    private MathDatabase mathDB;

    /**
     * The constructor for ExpressionDao takes MathDatabase as a parameter.
     *
     * @param mathDB a MathDatabase
     */
    public ExpressionDao(MathDatabase mathDB) {
        this.mathDB = mathDB;
    }

    /**
     * The method inserts a single expression into Expression table, and
     * implements the the method save that MathDao requires.
     *
     * @param symbolicExpression as a String to be searched for
     * @return true or false
     * @throws SQLException if an exception happens
     */
    @Override
    public boolean save(String symbolicExpression) throws SQLException {
        if (symbolicExpression.length() <= 1000) {
            try (Connection conn = mathDB.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO Expression (symbols) VALUES (?)")) {
                stmt.setString(1, symbolicExpression);
                stmt.executeUpdate();

                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * The method inserts all Strings that it gets as a parameter into the
     * Expression table.
     *
     * @param symbolicExpressions expressions as an ArrayList of Strings
     * @return true if successful else throws an exception
     * @throws SQLException if an exception happens
     */
    public boolean saveAll(List<String> symbolicExpressions) throws SQLException {
        try (Connection conn = mathDB.getConnection()) {
            for (int i = 0; i < symbolicExpressions.size(); i++) {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Expression (symbols) VALUES (?)")) {
                    String s = symbolicExpressions.get(i);
                    if (s.length() <= 1000) {
                        stmt.setString(1, s);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    throw new SQLException();
                }
            }

            return true;
        }
    }

    /**
     * The method deletes an Expression based on its primary key, which it gets
     * as parameter. One of the implementations of the required methods of
     * MathDao interface.
     *
     * @param id is the primary key of the Expression in question
     * @return true if successful else throws an exception
     * @throws SQLException if an exception happens
     */
    @Override
    public boolean delete(Integer id) throws SQLException {
        try (Connection conn = mathDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Expression WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return true;
        }
    }

    /**
     * Finds all Expressions that match the provided String
     *
     * @param partialExpression partial expression to be searhed for as a String
     * @return all matching Expressions as an ArrayList of Expressions
     * @throws SQLException if an exception happens
     */
    public ArrayList<Expression> findMatches(String partialExpression) throws SQLException {
        ArrayList<Expression> foundExpressions = new ArrayList<>();

        try (Connection conn = mathDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Expression WHERE symbols LIKE ?")) {
            stmt.setString(1, "%" + partialExpression + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    foundExpressions.add(new Expression(rs.getInt("id"), rs.getString("symbols")));
                }
            } catch (SQLException e) {
                throw new SQLException();
            }
        }

        return foundExpressions;
    }

    /**
     * Finds all Expressions in the table Expression.
     *
     * @return all the Expressions as an ArrayList of Expressions
     * @throws SQLException if an an exception happens
     */
    @Override
    public ArrayList<Expression> findAll() throws SQLException {
        ArrayList<Expression> expressions = new ArrayList<>();
        try (Connection conn = mathDB.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM Expression").executeQuery()) {

            while (rs.next()) {
                expressions.add(new Expression(rs.getInt("id"), rs.getString("symbols")));
            }
        }

        return expressions;
    }

    /**
     * Counts how many Expressions are saved in the Expression table.
     *
     * @return count of the expressions as an int value
     * @throws SQLException if an exception happens
     */
    public int countExpressionsInDatabase() throws SQLException {
        int count = 0;
        try (Connection conn = mathDB.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT COUNT(*) FROM Expression").executeQuery()) {

            count = rs.getInt(1);
        }

        return count;
    }
}
