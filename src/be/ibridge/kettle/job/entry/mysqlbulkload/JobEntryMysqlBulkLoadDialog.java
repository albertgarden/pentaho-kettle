/**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

/*
 * Created on 19-jun-2003
 *
 */

package be.ibridge.kettle.job.entry.mysqlbulkload;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import be.ibridge.kettle.core.Const;
import be.ibridge.kettle.core.Props;
import be.ibridge.kettle.core.WindowProperty;
import be.ibridge.kettle.core.database.DatabaseMeta;
import be.ibridge.kettle.core.dialog.DatabaseDialog;
import be.ibridge.kettle.core.widget.TextVar;
import be.ibridge.kettle.job.JobMeta;
import be.ibridge.kettle.job.dialog.JobDialog;
import be.ibridge.kettle.job.entry.JobEntryDialogInterface;
import be.ibridge.kettle.job.entry.JobEntryInterface;
import be.ibridge.kettle.trans.step.BaseStepDialog;
import be.ibridge.kettle.core.util.StringUtil;
import org.eclipse.swt.widgets.FileDialog;

/**
 * This dialog allows you to edit the Table Exists job entry settings. (select the connection and
 * the table to be checked) This entry type evaluates!
 * 
 * @author Matt
 * @since 19-06-2003
 */
public class JobEntryMysqlBulkLoadDialog extends Dialog implements JobEntryDialogInterface
{

	private static final String[] FILETYPES = new String[] 
		{
			Messages
			.getString("JobMysqlBulkLoad.Filetype.Text"),
			Messages
			.getString("JobMysqlBulkLoad.Filetype.All") };

	private Label wlName;

	private Text wName;

	private FormData fdlName, fdName;

	private Label wlConnection;

	private CCombo wConnection;

	private Button wbConnection;

	private FormData fdlConnection, fdbConnection, fdConnection;

	private Label wlTablename;

	private TextVar wTablename;

	private FormData fdlTablename, fdTablename;

	private Button wOK, wCancel;

	private Listener lsOK, lsCancel;

	private JobEntryMysqlBulkLoad jobEntry;

	private JobMeta jobMeta;

	private Shell shell;

	private Props props;

	private SelectionAdapter lsDef;

	private boolean changed;

	//Fichier
	private Label wlFilename;

	private Button wbFilename;

	private TextVar wFilename;

	private FormData fdlFilename, fdbFilename, fdFilename;

	//  LocalInfile
	private Label        wlLocalInfile;
	private Button       wLocalInfile;
	private FormData     fdlLocalInfile, fdLocalInfile;



	//Separator
	private Label wlSeparator;

	private TextVar wSeparator;

	private FormData fdlSeparator, fdSeparator;

	//List Columns

	private Label wlListattribut;

	private TextVar wListattribut;

	private FormData fdlListattribut, fdListattribut;


	

	//Ignore First lines
	private Label wlIgnorelines;

	private TextVar wIgnorelines;

	private FormData fdlIgnorelines, fdIgnorelines;

	//Replace
	private Label        wlReplacedata;
	private Button       wReplacedata;
	private FormData     fdlReplacedata, fdReplacedata;


	// Priority
	private Label wlProrityValue;
	private  CCombo wProrityValue;
	private FormData fdlProrityValue, fdProrityValue;

	public JobEntryMysqlBulkLoadDialog(Shell parent, JobEntryMysqlBulkLoad jobEntry, JobMeta jobMeta)
	{
		super(parent, SWT.NONE);
		props = Props.getInstance();
		this.jobEntry = jobEntry;
		this.jobMeta = jobMeta;

		if (this.jobEntry.getName() == null)
			this.jobEntry.setName(Messages.getString("JobMysqlBulkLoad.Name.Default"));
	}

	public JobEntryInterface open()
	{
		Shell parent = getParent();
		Display display = parent.getDisplay();

		shell = new Shell(parent, props.getJobsDialogStyle());
		props.setLook(shell);
		JobDialog.setShellImage(shell, jobEntry);

		ModifyListener lsMod = new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				jobEntry.setChanged();
			}
		};
		changed = jobEntry.hasChanged();

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Messages.getString("JobMysqlBulkLoad.Title"));

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Filename line
		wlName = new Label(shell, SWT.RIGHT);
		wlName.setText(Messages.getString("JobMysqlBulkLoad.Name.Label"));
		props.setLook(wlName);
		fdlName = new FormData();
		fdlName.left = new FormAttachment(0, 0);
		fdlName.right = new FormAttachment(middle, 0);
		fdlName.top = new FormAttachment(0, margin);
		wlName.setLayoutData(fdlName);
		wName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wName);
		wName.addModifyListener(lsMod);
		fdName = new FormData();
		fdName.left = new FormAttachment(middle, 0);
		fdName.top = new FormAttachment(0, margin);
		fdName.right = new FormAttachment(100, 0);
		wName.setLayoutData(fdName);

		// Connection line
		wlConnection = new Label(shell, SWT.RIGHT);
		wlConnection.setText(Messages.getString("JobMysqlBulkLoad.Connection.Label"));
		props.setLook(wlConnection);
		fdlConnection = new FormData();
		fdlConnection.left = new FormAttachment(0, 0);
		fdlConnection.top = new FormAttachment(wName, margin);
		fdlConnection.right = new FormAttachment(middle, -margin);
		wlConnection.setLayoutData(fdlConnection);

		wbConnection = new Button(shell, SWT.PUSH);
		wbConnection.setText(Messages.getString("System.Button.New") + "...");
		wbConnection.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				DatabaseMeta databaseMeta = new DatabaseMeta();
				DatabaseDialog cid = new DatabaseDialog(shell, databaseMeta);
				if (cid.open() != null)
				{
					jobMeta.addDatabase(databaseMeta);

					// SB: Maybe do the same her as in BaseStepDialog: remove
					// all db connections and add them again.
					wConnection.add(databaseMeta.getName());
					wConnection.select(wConnection.getItemCount() - 1);
				}
			}
		});
		fdbConnection = new FormData();
		fdbConnection.right = new FormAttachment(100, 0);
		fdbConnection.top = new FormAttachment(wName, margin);
		fdbConnection.height = 20;
		wbConnection.setLayoutData(fdbConnection);

		wConnection = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
		props.setLook(wConnection);
		for (int i = 0; i < jobMeta.nrDatabases(); i++)
		{
			DatabaseMeta ci = jobMeta.getDatabase(i);
			wConnection.add(ci.getName());
		}
		wConnection.select(0);
		wConnection.addModifyListener(lsMod);
		fdConnection = new FormData();
		fdConnection.left = new FormAttachment(middle, 0);
		fdConnection.top = new FormAttachment(wName, margin);
		fdConnection.right = new FormAttachment(wbConnection, -margin);
		wConnection.setLayoutData(fdConnection);

		// Table name line
		wlTablename = new Label(shell, SWT.RIGHT);
		wlTablename.setText(Messages.getString("JobMysqlBulkLoad.Tablename.Label"));
		props.setLook(wlTablename);
		fdlTablename = new FormData();
		fdlTablename.left = new FormAttachment(0, 0);
		fdlTablename.right = new FormAttachment(middle, 0);
		fdlTablename.top = new FormAttachment(wConnection, margin);
		wlTablename.setLayoutData(fdlTablename);

		wTablename = new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wTablename);
		wTablename.addModifyListener(lsMod);
		fdTablename = new FormData();
		fdTablename.left = new FormAttachment(middle, 0);
		fdTablename.top = new FormAttachment(wConnection, margin);
		fdTablename.right = new FormAttachment(100, 0);
		wTablename.setLayoutData(fdTablename);




		// Filename line
		wlFilename = new Label(shell, SWT.RIGHT);
		wlFilename.setText(Messages.getString("JobMysqlBulkLoad.Filename.Label"));
		props.setLook(wlFilename);
		fdlFilename = new FormData();
		fdlFilename.left = new FormAttachment(0, 0);
		fdlFilename.top = new FormAttachment(wTablename, margin);
		fdlFilename.right = new FormAttachment(middle, -margin);
		wlFilename.setLayoutData(fdlFilename);

		wbFilename = new Button(shell, SWT.PUSH | SWT.CENTER);
		props.setLook(wbFilename);
		wbFilename.setText(Messages.getString("System.Button.Browse"));
		fdbFilename = new FormData();
		fdbFilename.right = new FormAttachment(100, 0);
		fdbFilename.top = new FormAttachment(wTablename, 0);
		// fdbFilename.height = 22;
		wbFilename.setLayoutData(fdbFilename);

		wFilename = new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFilename);
		wFilename.addModifyListener(lsMod);
		fdFilename = new FormData();
		fdFilename.left = new FormAttachment(middle, 0);
		fdFilename.top = new FormAttachment(wTablename, margin);
		fdFilename.right = new FormAttachment(wbFilename, -margin);
		wFilename.setLayoutData(fdFilename);


		// Whenever something changes, set the tooltip to the expanded version:
		wFilename.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				wFilename.setToolTipText(StringUtil.environmentSubstitute(wFilename.getText()));
			}
		});

		wbFilename.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.txt", "*.csv", "*" });
				if (wFilename.getText() != null)
				{
					dialog.setFileName(StringUtil.environmentSubstitute(wFilename.getText()));
				}
				dialog.setFilterNames(FILETYPES);
				if (dialog.open() != null)
				{
					wFilename.setText(dialog.getFilterPath() + Const.FILE_SEPARATOR
						+ dialog.getFileName());
				}
			}
		});


		//Local
		wlLocalInfile = new Label(shell, SWT.RIGHT);
		wlLocalInfile.setText(Messages.getString("JobMysqlBulkLoad.LocalInfile.Label"));
		props.setLook(wlLocalInfile);
		fdlLocalInfile = new FormData();
		fdlLocalInfile.left = new FormAttachment(0, 0);
		fdlLocalInfile.top = new FormAttachment(wFilename, margin);
		fdlLocalInfile.right = new FormAttachment(middle, -margin);
		wlLocalInfile.setLayoutData(fdlLocalInfile);
		wLocalInfile = new Button(shell, SWT.CHECK);
		props.setLook(wLocalInfile);
		wLocalInfile.setToolTipText(Messages.getString("JobMysqlBulkLoad.LocalInfile.Tooltip"));
		fdLocalInfile = new FormData();
		fdLocalInfile.left = new FormAttachment(middle, 0);
		fdLocalInfile.top = new FormAttachment(wFilename, margin);
		fdLocalInfile.right = new FormAttachment(100, 0);
		wLocalInfile.setLayoutData(fdLocalInfile);
		wLocalInfile.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				jobEntry.setChanged();
			}
		});



		// Priority
		wlProrityValue = new Label(shell, SWT.RIGHT);
		wlProrityValue.setText(Messages.getString("JobMysqlBulkLoad.ProrityValue.Label"));
		props.setLook(wlProrityValue);
		fdlProrityValue = new FormData();
		fdlProrityValue.left = new FormAttachment(0, 0);
		fdlProrityValue.right = new FormAttachment(middle, 0);
		fdlProrityValue.top = new FormAttachment(wLocalInfile, margin);
		wlProrityValue.setLayoutData(fdlProrityValue);
		wProrityValue = new CCombo(shell, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
					wProrityValue.add(Messages.getString("JobMysqlBulkLoad.NorProrityValue.Label"));
					wProrityValue.add(Messages.getString("JobMysqlBulkLoad.LowProrityValue.Label"));
					wProrityValue.add(Messages.getString("JobMysqlBulkLoad.ConProrityValue.Label"));
					wProrityValue.select(0); // +1: starts at -1

		props.setLook(wProrityValue);
		fdProrityValue= new FormData();
		fdProrityValue.left = new FormAttachment(middle, 0);
		fdProrityValue.top = new FormAttachment(wLocalInfile, margin);
		fdProrityValue.right = new FormAttachment(100, 0);
		wProrityValue.setLayoutData(fdProrityValue);

		fdProrityValue = new FormData();
		fdProrityValue.left = new FormAttachment(middle, 0);
		fdProrityValue.top = new FormAttachment(wLocalInfile, margin);
		fdProrityValue.right = new FormAttachment(100, 0);
		wProrityValue.setLayoutData(fdProrityValue);




		// Separator
		wlSeparator = new Label(shell, SWT.RIGHT);
		wlSeparator.setText(Messages.getString("JobMysqlBulkLoad.Separator.Label"));
		props.setLook(wlSeparator);
		fdlSeparator = new FormData();
		fdlSeparator.left = new FormAttachment(0, 0);
		fdlSeparator.right = new FormAttachment(middle, 0);
		fdlSeparator.top = new FormAttachment(wProrityValue, margin);
		wlSeparator.setLayoutData(fdlSeparator);

		wSeparator = new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wSeparator);
		wSeparator.addModifyListener(lsMod);
		fdSeparator = new FormData();
		fdSeparator.left = new FormAttachment(middle, 0);
		fdSeparator.top = new FormAttachment(wProrityValue, margin);
		fdSeparator.right = new FormAttachment(100, 0);
		wSeparator.setLayoutData(fdSeparator);



		// List of columns to set for
		wlListattribut = new Label(shell, SWT.RIGHT);
		wlListattribut.setText(Messages.getString("JobMysqlBulkLoad.Listattribut.Label"));
		props.setLook(wlListattribut);
		fdlListattribut = new FormData();
		fdlListattribut.left = new FormAttachment(0, 0);
		fdlListattribut.right = new FormAttachment(middle, 0);
		fdlListattribut.top = new FormAttachment(wSeparator, margin);
		wlListattribut.setLayoutData(fdlListattribut);

		wListattribut = new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wListattribut);
		wListattribut.setToolTipText(Messages.getString("JobMysqlBulkLoad.Listattribut.Tooltip"));
		wListattribut.addModifyListener(lsMod);
		fdListattribut = new FormData();
		fdListattribut.left = new FormAttachment(middle, 0);
		fdListattribut.top = new FormAttachment(wSeparator, margin);
		fdListattribut.right = new FormAttachment(100, 0);
		wListattribut.setLayoutData(fdListattribut);



		//Replace data
		wlReplacedata = new Label(shell, SWT.RIGHT);
		wlReplacedata.setText(Messages.getString("JobMysqlBulkLoad.Replacedata.Label"));
		props.setLook(wlReplacedata);
		fdlReplacedata = new FormData();
		fdlReplacedata.left = new FormAttachment(0, 0);
		fdlReplacedata.top = new FormAttachment(wListattribut, margin);
		fdlReplacedata.right = new FormAttachment(middle, -margin);
		wlReplacedata.setLayoutData(fdlReplacedata);
		wReplacedata = new Button(shell, SWT.CHECK);
		props.setLook(wReplacedata);
		wReplacedata.setToolTipText(Messages.getString("JobMysqlBulkLoad.Replacedata.Tooltip"));
		fdReplacedata = new FormData();
		fdReplacedata.left = new FormAttachment(middle, 0);
		fdReplacedata.top = new FormAttachment(wListattribut, margin);
		fdReplacedata.right = new FormAttachment(100, 0);
		wReplacedata.setLayoutData(fdReplacedata);
		wReplacedata.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				jobEntry.setChanged();
			}
		});





		// Nbr of lines to ignore
		wlIgnorelines = new Label(shell, SWT.RIGHT);
		wlIgnorelines.setText(Messages.getString("JobMysqlBulkLoad.Ignorelines.Label"));
		props.setLook(wlIgnorelines);
		fdlIgnorelines = new FormData();
		fdlIgnorelines.left = new FormAttachment(0, 0);
		fdlIgnorelines.right = new FormAttachment(middle, 0);
		fdlIgnorelines.top = new FormAttachment(wReplacedata, margin);
		wlIgnorelines.setLayoutData(fdlIgnorelines);

		wIgnorelines = new TextVar(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wIgnorelines);
		wIgnorelines.addModifyListener(lsMod);
		fdIgnorelines = new FormData();
		fdIgnorelines.left = new FormAttachment(middle, 0);
		fdIgnorelines.top = new FormAttachment(wReplacedata, margin);
		fdIgnorelines.right = new FormAttachment(100, 0);
		wIgnorelines.setLayoutData(fdIgnorelines);






		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK"));
		FormData fd = new FormData();
		fd.right = new FormAttachment(50, -10);
		fd.bottom = new FormAttachment(100, 0);
		fd.width = 100;
		wOK.setLayoutData(fd);

		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel"));
		fd = new FormData();
		fd.left = new FormAttachment(50, 10);
		fd.bottom = new FormAttachment(100, 0);
		fd.width = 100;
		wCancel.setLayoutData(fd);

		// Add listeners
		lsCancel = new Listener()
		{
			public void handleEvent(Event e)
			{
				cancel();
			}
		};
		lsOK = new Listener()
		{
			public void handleEvent(Event e)
			{
				ok();
			}
		};

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);

		lsDef = new SelectionAdapter()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{
				ok();
			}
		};

		wName.addSelectionListener(lsDef);
		wTablename.addSelectionListener(lsDef);

		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(new ShellAdapter()
		{
			public void shellClosed(ShellEvent e)
			{
				cancel();
			}
		});

		getData();

		BaseStepDialog.setSize(shell);

		shell.open();
		props.setDialogSize(shell, "JobMysqlBulkLoadDialogSize");
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		return jobEntry;
	}

	public void dispose()
	{
		WindowProperty winprop = new WindowProperty(shell);
		props.setScreen(winprop);
		shell.dispose();
	}

	/**
	 * Copy information from the meta-data input to the dialog fields.
	 */
	public void getData()
	{
		// System.out.println("evaluates: "+jobentry.evaluates());

		if (jobEntry.getName() != null)
			wName.setText(jobEntry.getName());
		if (jobEntry.getTablename() != null)
			wTablename.setText(jobEntry.getTablename());
		if (jobEntry.getFilename() != null)
			wFilename.setText(jobEntry.getFilename());
		if (jobEntry.getSeparator() != null)
			wSeparator.setText(jobEntry.getSeparator());
	
				
		wReplacedata.setSelection(jobEntry.isReplacedata());
		
		wLocalInfile.setSelection(jobEntry.isLocalInfile());
		

		if (jobEntry.getIgnorelines() != null)
		{

			wIgnorelines.setText(jobEntry.getIgnorelines());

		}
		else
			wIgnorelines.setText("0");
		
		if (jobEntry.getListattribut() != null)
			wListattribut.setText(jobEntry.getListattribut());
		
     
		if (jobEntry.prorityvalue>=0) 
        {
            wProrityValue.select(jobEntry.prorityvalue );
        }
        else
        {
            wProrityValue.select(0); // NORMAL priority
        }

	
		
		if (jobEntry.getDatabase() != null)
		{
			wConnection.setText(jobEntry.getDatabase().getName());
		}
		wName.selectAll();
	}

	private void cancel()
	{
		jobEntry.setChanged(changed);
		jobEntry = null;
		dispose();
	}

	private void ok()
	{
		jobEntry.setName(wName.getText());
		jobEntry.setDatabase(jobMeta.findDatabase(wConnection.getText()));
		jobEntry.setTablename(wTablename.getText());
		jobEntry.setFilename(wFilename.getText());
		jobEntry.setSeparator(wSeparator.getText());
		jobEntry.setReplacedata(wReplacedata.getSelection());
		jobEntry.setIgnorelines(wIgnorelines.getText());
		jobEntry.setListattribut(wListattribut.getText());

		jobEntry.prorityvalue = wProrityValue.getSelectionIndex();

		jobEntry.setLocalInfile(wLocalInfile.getSelection());

		dispose();
	}

	public String toString()
	{
		return this.getClass().getName();
	}
}
