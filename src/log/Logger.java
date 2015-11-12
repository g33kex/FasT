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

