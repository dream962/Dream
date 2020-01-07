package com.deploy.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.deploy.util.Config;
import com.deploy.util.DirectoryUtil;
import com.deploy.util.FileHandler;

/**
 */
public class MainFrame
{
    // 显示当前进度文件
    Label labelProgress;

    // 进度条
    ProgressBar progressBar;

    // 当前目录
    Text textCur;

    // 上一版本目录
    Text textLast;

    // 要生成版本文件的目录
    Text textVer;
    
    // 增量包目录
    Text textIncrement;

    // 增量日志表格
    Table tableIncrement;

    // 主窗体
    Shell shell;

    // 回调
    Display display;

    // 当前目录浏览
    Button buttonCur;

    // 上一版本目录浏览
    Button buttonLast;

    // 需要做版本文件的目录
    Button buttonVer;
    
    // 增量包目录浏览
    Button buttonIncrement;

    // 恢复默认
    Button buttonDefault;

    // 统计耗时
    Label labelStat;

    DirectoryUtil directoryUtil;

    FileHandler fileHandler;

    List<File> fileList;

    Runnable runnable;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    public MainFrame()
    {
        super();
        init();
    }

    /**
     * 初始化控件
     */
    private void init()
    {
        display = new Display();
        shell = new Shell(display);

        shell.setText("打包神器");
        shell.setSize(1200, 700);
        shell.setLayout(new FillLayout());

        Group group = new Group(shell, SWT.NONE);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setLayout(new GridLayout(1, false));
        group.setText("file path select");

        Label label = new Label(group, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        label.setText("当前版本目录:");

        Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        composite.setLayout(new GridLayout(2, false));

        textCur = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER
                | SWT.READ_ONLY);
        textCur.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        buttonCur = new Button(composite, SWT.PUSH);
        buttonCur.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        buttonCur.setText("浏览");
        buttonCur.addMouseListener(new MouseLisnterImp(textCur, shell));

        Label labelLast = new Label(group, SWT.NONE);
        labelLast.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        labelLast.setText("上一版本目录:");

        Composite compositeLast = new Composite(group, SWT.NONE);
        compositeLast.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false));
        compositeLast.setLayout(new GridLayout(2, false));

        textLast = new Text(compositeLast, SWT.SINGLE | SWT.LEAD | SWT.BORDER
                | SWT.READ_ONLY);
        textLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        buttonLast = new Button(compositeLast, SWT.PUSH);
        buttonLast.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        buttonLast.setText("浏览");
        buttonLast.addMouseListener(new MouseLisnterImp(textLast, shell));

        Label labelVer = new Label(group, SWT.NONE);
        labelVer.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
                false, false));
        labelVer.setText("需要生成版本文件目录(一般为当前版本目录的子目录):");
        
        Composite compositeVer = new Composite(group, SWT.NONE);
        compositeVer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false));
        compositeVer.setLayout(new GridLayout(2, false));

        textVer = new Text(compositeVer, SWT.SINGLE | SWT.LEAD | SWT.BORDER
                | SWT.READ_ONLY);
        textVer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        buttonVer = new Button(compositeVer, SWT.PUSH);
        buttonVer.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        buttonVer.setText("浏览");
        buttonVer.addMouseListener(new MouseLisnterImp(textVer, shell));

        
        
        Label labelIncrement = new Label(group, SWT.NONE);
        labelIncrement.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
                false, false));
        labelIncrement.setText("增量包目录:");

        Composite compositeIncrement = new Composite(group, SWT.NONE);
        compositeIncrement.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false));
        compositeIncrement.setLayout(new GridLayout(2, false));

        textIncrement = new Text(compositeIncrement, SWT.SINGLE | SWT.LEAD
                | SWT.BORDER | SWT.READ_ONLY);
        textIncrement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));

        buttonIncrement = new Button(compositeIncrement, SWT.PUSH);
        buttonIncrement.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
                false, false));
        buttonIncrement.setText("浏览");
        buttonIncrement.addMouseListener(new MouseLisnterImp(textIncrement,
                shell));

        labelProgress = new Label(group, SWT.NONE);
        labelProgress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false));
        labelProgress.setText("当前进度(0%):");

        progressBar = new ProgressBar(group, SWT.BORDER);
        progressBar
                .setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        tableIncrement = new Table(group, SWT.SINGLE | SWT.FULL_SELECTION
                | SWT.BORDER);
        tableIncrement.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true));
        tableIncrement.setLinesVisible(true);
        tableIncrement.setHeaderVisible(true);

        TableColumn tableColumn = new TableColumn(tableIncrement, SWT.CENTER);
        tableColumn.setAlignment(SWT.CENTER);
        tableColumn.setWidth(300);
        tableColumn
                .setText("                              文件路径                                                                                                                                          ");

        TableColumn tableColumn2 = new TableColumn(tableIncrement, SWT.NONE);
        tableColumn2.setText("  状态    ");
        tableColumn2.setWidth(100);

        for (int i = 0; i < tableIncrement.getColumnCount(); i++)
        {
            tableIncrement.getColumn(i).pack();
        }

        Composite compositeStart = new Composite(group, SWT.NONE);
        compositeStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false));
        compositeStart.setLayout(new GridLayout(3, false));

        buttonDefault = new Button(compositeStart, SWT.PUSH);
        buttonDefault.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
                false, false));
        buttonDefault.setText("恢复默认");
        buttonDefault.addMouseListener(new BtnDefaultEvent());

        Button btnStart = new Button(compositeStart, SWT.PUSH);
        btnStart.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        btnStart.setText("开始执行");
        btnStart.addMouseListener(new BtnStartEvent());

        Button btnOpenInrDir = new Button(compositeStart, SWT.PUSH);
        btnOpenInrDir.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        btnOpenInrDir.setText("增量包目录");
        btnOpenInrDir.addMouseListener(new BtnOpenInrDirEvent());
        
        labelStat = new Label(group, SWT.NONE);
        labelStat.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        labelStat.setText("耗时:0 ms");

        String mainDeployPath = textCur.getText();
        String lastDeployPath = textLast.getText();
        String incrementDeployPath = textIncrement.getText();
        String verPath = textVer.getText();
        String exceptString = Config.getValue("filterFileName");

        fileHandler = new FileHandler(mainDeployPath, lastDeployPath,
                incrementDeployPath, verPath);
        directoryUtil = new DirectoryUtil(fileHandler, exceptString);
        fileList = new ArrayList<File>();

        runnable = new WorkThread();
    }

    public void setCurDirector(String text)
    {
        if (textCur == null)
        {
            return;
        }
        textCur.setText(text);
    }

    public void setLastDirector(String text)
    {
        if (textLast == null)
        {
            return;
        }
        textLast.setText(text);
    }

    public void setIncrementDirector(String text)
    {
        if (textIncrement == null)
        {
            return;
        }
        textIncrement.setText(text);
    }

    public void addTableItem(String[] strings)
    {
        TableItem tableItem = new TableItem(tableIncrement, SWT.NONE);
        tableItem.setText(strings);
    }

    public void start()
    {
        shell.open();

        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        display.dispose();
    }

    private class BtnDefaultEvent implements MouseListener
    {
        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
         * .swt.events.MouseEvent)
         */
        @Override
        public void mouseDoubleClick(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseDown(MouseEvent e)
        {
            String mainDeployPath = Config.getValue("curdeploy");
            String lastDeployPath = Config.getValue("lastdeploy");
            String incrementDeployPath = Config.getValue("incrementdeploy");
            String verPath = Config.getValue("verDir");
            
            setCurDirector(mainDeployPath);
            setLastDirector(lastDeployPath);
            setIncrementDirector(incrementDeployPath);
            setVerDirector(verPath);
            
            clear();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseUp(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }
    }

    private class BtnStartEvent implements MouseListener
    {

        public BtnStartEvent()
        {
            super();

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
         * .swt.events.MouseEvent)
         */
        @Override
        public void mouseDoubleClick(MouseEvent e)
        {

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseDown(MouseEvent e)
        {
            startThread();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseUp(MouseEvent e)
        {

        }

    }

    private class BtnOpenInrDirEvent implements MouseListener
    {

        public BtnOpenInrDirEvent()
        {
            super();

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
         * .swt.events.MouseEvent)
         */
        @Override
        public void mouseDoubleClick(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseDown(MouseEvent e)
        {
            try
            {
                String inrDirPath = textIncrement.getText();
                Runtime.getRuntime().exec("cmd.exe /c start " + inrDirPath);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
         * .MouseEvent)
         */
        @Override
        public void mouseUp(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

    }

    
    private void startThread()
    {
        final Runnable refreshRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        shell.getDisplay().asyncExec(new WorkThread());
                    }
                });
                thread.start();
            }
        };
        BusyIndicator.showWhile(shell.getDisplay(), refreshRunnable);
    }

    private class WorkThread implements Runnable
    {
        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run()
        {
            btnEnable(false);
            clear();
            String mainDeployPath = textCur.getText();
            String lastDeployPath = textLast.getText();
            String incrementDeployPath = textIncrement.getText();
            String verPath = textVer.getText();
            
            fileHandler.setRootCurDire(mainDeployPath);
            fileHandler.setRootOldDire(lastDeployPath);
            fileHandler.setRootIncrementDire(incrementDeployPath);
            fileHandler.setRootVerDire(verPath);
            
            fileList.clear();
            directoryUtil.refreshFileList(mainDeployPath, fileList);
            int total = fileList.size();
            progressBar.setMaximum(total);
            progressBar.setMinimum(0);

            int i = 0;

            long timeStart = System.currentTimeMillis();
            for (final File file : fileList)
            {
                labelProgress.setText("当前进度(" + (i + 1) * 100 / total + "%):"
                        + file.getAbsolutePath());

                int result = 0;
                result = fileHandler.done(file);
                String[] strings = new String[2];
                if (result == 1) // 新增文件
                {
                    strings[0] = file.getAbsolutePath();
                    strings[1] = "新增";
                    addTableItem(strings);
                }
                else if (result == 2) // 修改的文件
                {
                    strings[0] = file.getAbsolutePath();
                    strings[1] = "修改";
                    addTableItem(strings);
                }
                i++;
                progressBar.setSelection(i);
            }

            // 生成md5摘要文件
            fileHandler.createMd5File();
            
            long timeEnd = System.currentTimeMillis();
            labelStat.setText("耗时:" + (timeEnd - timeStart) + " ms");
            
            btnEnable(true);
        }

    }

    private void btnEnable(boolean enable)
    {
        buttonCur.setEnabled(enable);
        buttonLast.setEnabled(enable);
        buttonIncrement.setEnabled(enable);
        buttonDefault.setEnabled(enable);
    }

    private void clear()
    {
        progressBar.setSelection(0);
        fileHandler.init();
        tableIncrement.removeAll();
        labelProgress.setText("当前进度(0%):");
        labelStat.setText("耗时:0 ms");
    }

    /**
     * @param verPath
     */
    public void setVerDirector(String text)
    {
        if (textVer == null)
        {
            return;
        }
        textVer.setText(text);
    }

}
