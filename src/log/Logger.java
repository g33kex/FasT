/*
 *  FasT -- A Fast algorithm for simulaTions. 
 * 
 *  Copyright © 2016 Tourdetour
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Contact me by email : <TPEFasT@mail.com>
 */

package log;

public class Logger {
	
	private int level=0;
	
	//Log levels : 0 = all; 1 = info, warning, error; 2 = warning, error; 3 = error
	public Logger(int level)
	{
		this.level=level;
	}
	
	public Logger()
	{
		
	}
	
	public void debug(Object s)
	{
		this.log("[DEBUG]: " + this.convertToString(s), 1);
	}
	
	public void info(Object s)
	{
		this.log("[Info]: " + this.convertToString(s), 2);
	}
	
	public void warning(Object s)
	{
		this.log("[Warning]: " + this.convertToString(s), 3);
	}
	
	public void error(Object s)
	{
		this.log("[ERROR]: " + this.convertToString(s), 4);
	}
	
	private String convertToString(Object s)
	{
		return s.toString();
	}
	
	private void log(String s,int level)
	{
		if(level>this.level)
		{
			this.writeInConsole(s+"\n");
		}
	}
	
	private void writeInConsole(String s)
	{
		System.out.print(s);
	}
	
	
}

