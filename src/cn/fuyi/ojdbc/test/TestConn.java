package cn.fuyi.ojdbc.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.fuyi.ojdbc.utils.JDBCUtils;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class TestConn {

	/*
	 * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
   {call <procedure-name>[(<arg1>,<arg2>, ...)]}

	 * 
	 * 
	 */
	
	Connection conn = null;
	@Before
	public void init() {
		conn = JDBCUtils.getConnection();
	}
	
	@Test
	public void query() throws Exception {
		String sql = "select * from USERJD t";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet result = ps.executeQuery();
		while(result.next()) {
			String one = result.getString(2);
			System.out.println(one);
		}
	}
	
	@Test
	public void testProcedure() {
		String sql = "{call queryEmpInfo(?,?,?,?)}";
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall(sql);
			
			cs.setInt(1, 7839);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);
			cs.registerOutParameter(4, OracleTypes.NUMBER);
			
			cs.execute();
			
			String ename = cs.getString(2);
			String empjob = cs.getString(3);
			Double sal = cs.getDouble(4);
			
			System.out.println(ename + "\t" + empjob + "\t" + sal);
			
			
			
			
			
			
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, cs, null);
		} 
	}
	
	@Test
	public void testFunction() {
		String sql = "{?= call queryEmpIncomeOfYear(?)}";
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall(sql);
			
			cs.registerOutParameter(1, OracleTypes.NUMBER);
			cs.setInt(2, 7839);
			
			cs.execute();
			
			
			Double sal = cs.getDouble(1);
			
			System.out.println(sal);
			
			
			
			
			
			
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, cs, null);
		} 
	}
	
	@Test
	public void testProcedureCursor() {
		String sql = "{call mypackage.queryEmpList(?,?)}";
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			cs = conn.prepareCall(sql);
			
			cs.setInt(1, 10);
			cs.registerOutParameter(2, OracleTypes.CURSOR);
		
			cs.execute();
			
			rs = ((OracleCallableStatement)cs).getCursor(2);
			while(rs.next()) {
				String ename = rs.getString("ename");
				Double sal = rs.getDouble("sal");
				System.out.println(ename + "\t" + sal);
			}
			
			
			
			
			
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.release(conn, cs, null);
		} 
	}
	
	@After
	public void destory() {
		JDBCUtils.release(conn, null, null);
	}
}
