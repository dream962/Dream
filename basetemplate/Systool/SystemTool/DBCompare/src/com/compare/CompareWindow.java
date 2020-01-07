package com.compare;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.compare.CompareFactory.CompareData;
import com.compare.data.ConfigData;
import com.compare.data.DBSource;

public class CompareWindow extends ApplicationWindow
{
    /** 更新的数据源（修改） */
    public static DBSource updateDataSource;

    /** 对比的数据源（标准） */
    public static DBSource compareDataSource;

    private static final String PATH = "config/db.properties";

    public static ConfigData data = new ConfigData();

    private Text text;

    private TreeItem compareItem;

    private TreeItem updateItem;

    /** 修改的库（修改） */
    private static String SelectUpdateDB = "";

    /** 对比的table（标准） */
    private static String SelectCompareDB = "";
    private Text txt_update;
    private Text txt_compare;

    /**
     * Create the application window.
     */
    public CompareWindow()
    {
        super(null);
        init();
        createActions();
        addToolBar(SWT.FLAT | SWT.WRAP);
        addMenuBar();
        addStatusLine();
    }

    private void init()
    {
        Properties prop = new Properties();
        InputStream in = null;
        try
        {
            in = new FileInputStream(PATH);
            prop.load(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            try
            {
                in.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            System.exit(1);
        }

        data.data.driverName = prop.getProperty("driverName");
        data.data.url = prop.getProperty("url");
        data.data.userName = prop.getProperty("userName");
        data.data.password = prop.getProperty("password");

        data.compareData.driverName = prop.getProperty("driverName_c");
        data.compareData.url = prop.getProperty("url_c");
        data.compareData.userName = prop.getProperty("userName_c");
        data.compareData.password = prop.getProperty("password_c");

        updateDataSource = new DBSource(data.data);
        compareDataSource = new DBSource(data.compareData);
    }

    /**
     * Create contents of the application window.
     * 
     * @param parent
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        RowLayout rl_container = new RowLayout(SWT.VERTICAL);
        rl_container.fill = true;
        rl_container.justify = true;
        rl_container.center = true;
        rl_container.spacing = 0;
        rl_container.marginTop = 0;
        rl_container.marginRight = 0;
        rl_container.marginLeft = 0;
        rl_container.marginBottom = 0;
        container.setLayout(rl_container);
        {
            {
                Composite composite = new Composite(container, SWT.NONE);
                composite.setLayoutData(new RowData(SWT.DEFAULT, 114));
                composite.setLayout(new FillLayout(SWT.HORIZONTAL));
                {
                    Group group = new Group(composite, SWT.NONE);
                    group.setText("数据源");

                    txt_update = new Text(group, SWT.BORDER);
                    txt_update.setBounds(221, 26, 418, 23);
                    txt_update.setText(data.data.url);

                    txt_compare = new Text(group, SWT.BORDER);
                    txt_compare.setBounds(221, 55, 418, 23);
                    txt_compare.setText(data.compareData.url);

                    Label label = new Label(group, SWT.NONE);
                    label.setBounds(47, 29, 149, 17);
                    label.setText("修改库(要修改的数据库)：");

                    Label label_1 = new Label(group, SWT.NONE);
                    label_1.setBounds(47, 58, 160, 17);
                    label_1.setText("对比库(不能修改的数据库)：");

                    Button btnNewButton_3 = new Button(group, SWT.NONE);
                    btnNewButton_3.addSelectionListener(new SelectionAdapter()
                    {
                        @Override
                        public void widgetSelected(SelectionEvent e)
                        {
                            updateItem.removeAll();

                            if (txt_compare.getText().isEmpty() || txt_update.getText().isEmpty())
                            {
                                text.append("数据库连接字符串无效!\n");
                                return;
                            }

                            if (!data.data.url.equalsIgnoreCase(txt_update.getText()))
                            {
                                data.data.url = txt_update.getText();
                                updateDataSource = new DBSource(data.data);
                            }

                            if (!data.compareData.url.equalsIgnoreCase(txt_compare.getText()))
                            {
                                data.compareData.url = txt_compare.getText();
                                compareDataSource = new DBSource(data.compareData);
                            }

                            List<String> databases = updateDataSource.getDatabase();
                            for (int i = 0; i < databases.size(); i++)
                            {
                                TreeItem dbItem = new TreeItem(updateItem, SWT.NONE);
                                dbItem.setData("type", "DB");
                                dbItem.setText(databases.get(i));
                            }

                            compareItem.removeAll();
                            List<String> databases1 = compareDataSource.getDatabase();
                            for (int i = 0; i < databases1.size(); i++)
                            {
                                TreeItem dbItem = new TreeItem(compareItem, SWT.NONE);
                                dbItem.setData("type", "DB");
                                dbItem.setText(databases1.get(i));
                            }

                            text.append("数据库表结构加载完成！\n");
                        }
                    });
                    btnNewButton_3.setBounds(335, 84, 80, 27);
                    btnNewButton_3.setText("1:加载");
                }
            }

            {
                Composite composite = new Composite(container, SWT.NONE);
                composite.setLayoutData(new RowData(SWT.DEFAULT, 197));
                composite.setLayout(new FillLayout(SWT.HORIZONTAL));
                {
                    Tree updateTree = new Tree(composite, SWT.BORDER);

                    updateItem = new TreeItem(updateTree, SWT.NONE);
                    updateItem.setData("type", "conn");
                    updateItem.setText("修改库（被修改的数据库）");

                    updateTree.addListener(SWT.Selection, new Listener()
                    {
                        @Override
                        public void handleEvent(Event event)
                        {
                            SelectUpdateDB = "";
                            TreeItem[] selection = updateTree.getSelection();
                            if (selection.length > 0 && selection[0].getData("type").equals("DB"))
                            {
                                SelectUpdateDB = selection[0].getText();
                                text.append("当前选择的修改数据库：" + data.data.url + " -- " + SelectUpdateDB + "\n");
                            }
                        }
                    });
                }
                {
                    Tree compareTree = new Tree(composite, SWT.BORDER);
                    compareItem = new TreeItem(compareTree, SWT.NONE);
                    compareItem.setData("type", "conn");
                    compareItem.setText("对比库（标准参照库，不需要修改的数据库）");

                    compareTree.addListener(SWT.Selection, new Listener()
                    {
                        @Override
                        public void handleEvent(Event event)
                        {
                            SelectCompareDB = "";
                            TreeItem[] selection = compareTree.getSelection();
                            if (selection.length > 0 && selection[0].getData("type").equals("DB"))
                            {
                                SelectCompareDB = selection[0].getText();
                                text.append("当前选择的对比数据库：" + data.compareData.url + " -- " + SelectCompareDB + "\n");
                            }
                        }
                    });
                }
            }
        }

        {
            Composite composite = new Composite(container, SWT.NONE);
            composite.setLayoutData(new RowData(SWT.DEFAULT, 250));

            Group group = new Group(composite, SWT.NONE);
            group.setBounds(10, 10, 743, 240);

            Button btnNewButton = new Button(group, SWT.NONE);
            btnNewButton.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    if (SelectCompareDB.isEmpty() || SelectUpdateDB.isEmpty())
                    {
                        text.append("选择的表无效。\n");
                        return;
                    }

                    MessageBox messageBox = new MessageBox(group.getShell(), SWT.OK | SWT.CANCEL);
                    messageBox.setText("对比确认");
                    String str = "确定要修改【" + txt_update.getText() + " -- " + SelectUpdateDB + "】数据库里面的数据吗，请确认修改的库是否正确？";
                    messageBox.setMessage(str);
                    if (messageBox.open() == SWT.OK)
                    {
                        CompareData data = CompareFactory.compare(SelectCompareDB, SelectUpdateDB, text);
                        text.append(data.desc);

                        if (!data.sql.isEmpty())
                            updateDataSource.execute(SelectUpdateDB, data.sql);

                        text.append("修改数据库成功。");
                    }
                }
            });

            btnNewButton.setBounds(392, 10, 80, 27);
            btnNewButton.setText("4:对比修改");

            Button btnNewButton_2 = new Button(group, SWT.NONE);
            btnNewButton_2.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    if (SelectCompareDB.isEmpty() || SelectUpdateDB.isEmpty())
                    {
                        text.append("选择的表无效。\n");
                        return;
                    }

                    CompareData data = CompareFactory.compare(SelectCompareDB, SelectUpdateDB, text);
                    text.append(data.desc);
                    text.append("    =============================SQL===================================    ");
                    text.append(data.sql);
                }
            });
            btnNewButton_2.setBounds(275, 10, 80, 27);
            btnNewButton_2.setText("3:打印信息");

            text = new Text(group, SWT.MULTI | SWT.LEAD | SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
            text.setBounds(10, 43, 723, 192);
            text.setTopIndex(Integer.MAX_VALUE);
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            text.append("数据库对比工具：\n   1.先加载数据库表结构。\n   2.对比数据。\n");

            Button btnNewButton_1 = new Button(group, SWT.NONE);
            btnNewButton_1.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    text.setText("");
                }
            });
            btnNewButton_1.setBounds(498, 10, 80, 27);
            btnNewButton_1.setText("清除日志");

            Button button = new Button(group, SWT.NONE);
            button.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    if (SelectCompareDB.isEmpty() || SelectUpdateDB.isEmpty())
                    {
                        text.append("选择的表无效。\n");
                        return;
                    }
                    CompareFactory.init(SelectCompareDB, SelectUpdateDB, text);
                    text.append("数据库表结构加载完成。\n");
                }
            });
            button.setBounds(161, 10, 80, 27);
            button.setText("2:加载表信息");

            Button btnNewButton_4 = new Button(group, SWT.NONE);
            btnNewButton_4.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    String string = CompareFactory.deleteDB();
                    text.setText("**********数据库表数据清空代码*********\n" + string);
                }
            });
            btnNewButton_4.setBounds(614, 10, 80, 27);
            btnNewButton_4.setText("清空SQL");
        }

        return container;
    }

    /**
     * Create the actions.
     */
    private void createActions()
    {
        // Create the actions
    }

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String args[])
    {
        try
        {
            CompareWindow window = new CompareWindow();
            window.setBlockOnOpen(true);
            window.open();
            Display.getCurrent().dispose();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Configure the shell.
     * 
     * @param newShell
     */
    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setText("数据库对比工具");
    }

    /**
     * Return the initial size of the window.
     */
    @Override
    protected Point getInitialSize()
    {
        return new Point(769, 632);
    }
}
