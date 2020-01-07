package com.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tool.code.CommonUtil;
import com.tool.code.DaoCodeUtil;
import com.tool.code.FieldInfo;
import com.tool.data.AllSource;
import com.tool.data.ExcelFile;
import com.tool.data.ExcelSheet;

public class MainWindow
{
    public static int ORMFLAG = 0;
    public static Table table;

    public static Table _Table;

    public static Shell codeDialog;

    public static Tree tree;

    public static Map<String, FieldInfo> fieldMap;

    private static String selectExtMethod = "";

    public static String currentSelectedTable;

    public static boolean SELECT_ALL = false;
    /** 配置路径 */
    public static String propertiesPath = "";

    public static AllSource dataSource;

    /** 选择的数据方式：0：数据库；1：excel表 */
    public static int selectType = 0;

    public static final String[] titles = { "Name", "Java Type", "Sql Type", "Primary Key", "Auto", "Max Size",
            "Comment" };

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.err.println("请输入配置文件");
            return;
        }
        propertiesPath = args[0];

        dataSource = new AllSource(propertiesPath);

        /**
         * 初始化主窗口
         */
        final Display display = Display.getDefault();
        final Shell shell = new Shell();
        shell.setSize(1100, 700);
        shell.setText("Code-Tool");
        shell.setLayout(new FillLayout(SWT.HORIZONTAL));

        if (propertiesPath == null)
            return;

        // 数据库和excel数据源UI
        dbAndExcelSourceUI(shell);

        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayout(getGridLayout());

        SashForm sashForm = new SashForm(composite, SWT.BORDER);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite compConnList = new Composite(sashForm, SWT.BORDER);
        compConnList.setLayout(getGridLayout());

        // 左边-功能树编辑区
        leftTreeUI(compConnList);

        Composite compContentShow = new Composite(sashForm, SWT.BORDER);
        compContentShow.setLayout(getGridLayout());

        // 右编辑区功能条列表
        ToolBar codeBar = new ToolBar(compContentShow, SWT.FLAT | SWT.RIGHT);
        codeBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        // 选中全部UI
        selectAllUI(codeBar, shell, display);

        // excel表头、lua文件生成
        otherUI(codeBar, shell, display);

        // dao代码UI
        daoUI(codeBar, shell, display);

        // bean代码UI
        beanUI(codeBar, shell, display);

        // orm代码UI
        ormUI(codeBar, shell, display);

        // 右边-数据详细编辑表格区
        dataTableUI(compContentShow);

        sashForm.setWeights(new int[] { 1, 4 });

        shell.open();
        shell.layout();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }

    private static void dbAndExcelSourceUI(Shell shell)
    {
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        MenuItem muConn = new MenuItem(menu, SWT.CASCADE);
        muConn.setText("Source");
        Menu menu_1 = new Menu(muConn);
        muConn.setMenu(menu_1);
        MenuItem menuData = new MenuItem(menu_1, SWT.NONE);
        menuData.setText("加载 database");
        // 数据库初始化
        menuData.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                selectType = 0;
                tree.removeAll();
                dataSource.initDB();
                List<String> databases = dataSource.dBSource.getDatabaseOrExcel();
                for (int i = 0; i < databases.size(); i++)
                {
                    TreeItem dbItem = new TreeItem(tree, SWT.NONE);
                    dbItem.setData("type", "DB");
                    dbItem.setText(databases.get(i));

                    List<String> tables = dataSource.getTables(databases.get(i));

                    for (int j = 0; j < tables.size(); j++)
                    {
                        TreeItem treeItem = new TreeItem(dbItem, SWT.NONE);
                        treeItem.setText(tables.get(j));
                        treeItem.setData("type", "TABLE");
                    }
                }
            }
        });

        MenuItem menuExcel = new MenuItem(menu_1, SWT.NONE);
        menuExcel.setText("加载 excel");
        // excel初始化
        menuExcel.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                selectType = 1;
                tree.removeAll();
                dataSource.initExcel();
                List<String> databases = dataSource.excelSource.getDatabaseOrExcel();
                for (int i = 0; i < databases.size(); i++)
                {
                    TreeItem dbItem = new TreeItem(tree, SWT.NONE);
                    dbItem.setData("type", "DB");
                    dbItem.setText(databases.get(i));

                    List<String> tables = dataSource.getTables(databases.get(i));

                    if (tables != null)
                    {
                        for (int j = 0; j < tables.size(); j++)
                        {
                            TreeItem treeItem = new TreeItem(dbItem, SWT.NONE);
                            treeItem.setText(tables.get(j));
                            treeItem.setData("type", "TABLE");
                        }
                    }
                }
            }
        });

    }

    private static void leftTreeUI(Composite compConnList)
    {
        /** 左边竖直窗口 */
        Composite composite_1 = new Composite(compConnList, SWT.NONE);
        composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        CLabel lblNewLabel = new CLabel(composite_1, SWT.NONE);
        lblNewLabel.setText("Connections");

        /** 右边竖直窗口 */
        Composite composite_2 = new Composite(compConnList, SWT.NONE);
        composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        composite_2.setLayout(getGridLayout());

        tree = new Tree(composite_2, SWT.BORDER);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        /**
         * 添加树节点被点击的监听
         */
        tree.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                TreeItem[] selection = tree.getSelection();
                if (selection.length > 0 && selection[0].getData("type").equals("TABLE"))
                {
                    currentSelectedTable = selection[0].getText();
                    if (fieldMap != null)
                        fieldMap.clear();
                    table.removeAll();
                    fieldMap = dataSource.getTableFieldList(selection[0].getParentItem().getText(),
                            selection[0].getText());

                    List<FieldInfo> fieldList = new ArrayList<>();
                    fieldList.addAll(fieldMap.values());

                    for (FieldInfo field : fieldList)
                    {
                        TableItem t_item = new TableItem(table, SWT.NONE);
                        t_item.setText(0, field.getName());
                        t_item.setText(1, field.getJavaType());
                        t_item.setText(2, field.getSqlType());
                        t_item.setText(3, field.isPrimaryKey()
                                ? "主键" + (field.primaryKeyIndex > 0 ? field.primaryKeyIndex : "") : "");// 主键
                        t_item.setText(4, field.isAotuIncreamte() ? "自增" : "");// 是否自增
                        t_item.setText(5, field.getLen() + "");
                        t_item.setText(6, field.getComment());

                        // 自增的字段默认不算代码生成
                        if (field.isAotuIncreamte() == true)
                            t_item.setChecked(false);
                        else
                            t_item.setChecked(true);
                    }

                    for (int i = 0; i < titles.length; i++)
                    {
                        table.getColumn(i).pack();
                    }
                }
                else
                {
                    currentSelectedTable = selection[0].getText();
                }
            }
        });

    }

    private static void selectAllUI(ToolBar codeBar, Shell shell, Display display)
    {
        ToolItem tltmSelectall = new ToolItem(codeBar, SWT.NONE);
        tltmSelectall.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (currentSelectedTable == null || table == null)
                    return;
                SELECT_ALL = !SELECT_ALL;
                for (TableItem item : table.getItems())
                {
                    String clickItemName = item.getText();
                    item.setChecked(SELECT_ALL);
                    fieldMap.get(clickItemName).setFlag(SELECT_ALL);
                }
            }
        });
        tltmSelectall.setText("SelectAll");
    }

    private static void otherUI(ToolBar codeBar, Shell shell, Display display)
    {
        /**
         * 点击生成lua数据
         */
        ToolItem luaDataTool = new ToolItem(codeBar, SWT.NONE);
        luaDataTool.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (currentSelectedTable == null || table == null)
                    return;

                /* 生成代码预览窗口, 获取焦点 */
                codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setLayout(new FillLayout(SWT.VERTICAL));
                codeDialog.setText("提示：");

                final Label _Label = new Label(codeDialog, SWT.BORDER);
                _Label.setText("请选择需要拓展的方法");
                _Table = new Table(codeDialog, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

                final Composite _Composite = new Composite(codeDialog, SWT.BOTTOM);
                _Composite.setLayout(new FillLayout());
                final Button _SubmitButton = new Button(_Composite, SWT.BOTTOM);
                _SubmitButton.setText("生成");

                _SubmitButton.addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        for (TableItem item : _Table.getItems())
                        {
                            if (item.getChecked())
                            {
                                selectExtMethod = item.getText(0);
                            }
                        }

                        UILogicController.generateLuaCode(currentSelectedTable);

                        codeDialog.dispose();
                    }
                });
                codeDialog.setSize(600, 500);
                codeDialog.open();
            }
        });
        luaDataTool.setText("生成lua数据");

        /**
         * 点击 查看数值策划Excel表头
         */
        ToolItem codeDao = new ToolItem(codeBar, SWT.NONE);
        codeDao.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (currentSelectedTable == null || table == null)
                    return;
                /* 生成代码预览窗口, 获取焦点 */
                final Shell codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setLayout(new FillLayout());
                codeDialog.setText("Excel 表头");

                final Text text = new Text(codeDialog, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

                String[] strList = currentSelectedTable.split("_");
                String name = strList[strList.length - 1];

                String code = DaoCodeUtil.generateDaoImplCode(name, fieldMap);
                text.setText(code);

                codeDialog.setSize(600, 500);
                codeDialog.open();
            }
        });
        codeDao.setText("Excel表头");

    }

    private static void daoUI(ToolBar codeBar, Shell shell, Display display)
    {
        ToolItem codeCreate = new ToolItem(codeBar, SWT.NONE);
        codeCreate.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (selectType == 1 || currentSelectedTable == null || table == null)
                    return;

                /* 生成代码预览窗口, 获取焦点 */
                codeDialog = new Shell(shell, shell.getStyle());
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setSize(560, 320);
                codeDialog.setLayout(new FillLayout(SWT.HORIZONTAL));
                codeDialog.setText("提示：");

                Composite composite = new Composite(codeDialog, SWT.NONE);

                final Label _Label = new Label(composite, SWT.NONE);
                _Label.setText("请选择需要拓展的方法：");
                _Label.setBounds(10, 15, 300, 20);

                final Button check1 = new Button(composite, SWT.CHECK);
                check1.setBounds(10, 45, 460, 20);
                check1.setText("是否在数据库XXXInfo.setXXX方法生成addChangedMap方法");

                final Button check2 = new Button(composite, SWT.CHECK);
                check2.setBounds(10, 70, 460, 20);
                check2.setText("是否生成缓存Remote模块类");
                
                final Button check3 = new Button(composite, SWT.CHECK);
                check3.setBounds(10, 95, 460, 20);
                check3.setText("是否直连数据库操作");

                _Table = new Table(composite, SWT.CHECK | SWT.BORDER);
                _Table.setBounds(0, 125, 600, 100);

                TableItem _TableItem = new TableItem(_Table, SWT.NONE);
                _TableItem.setText(0, "getMultiByUserID---返回列表）");
                _TableItem.setChecked(true);

                TableItem _TableItem1 = new TableItem(_Table, SWT.NONE);
                _TableItem1.setText(0, "getSingleByUserID---返回单条");
                _TableItem1.setChecked(false);

                // 单选
                _Table.addListener(SWT.Selection, new Listener()
                {
                    public void handleEvent(Event event)
                    {
                        TableItem[] titem = _Table.getItems();
                        for (int i = 0; i < titem.length; i++)
                        {
                            if (!event.item.equals(titem[i]))
                            {
                                titem[i].setChecked(false);
                            }
                        }
                    }
                });

                final Button _SubmitButton = new Button(composite, SWT.NONE);
                _SubmitButton.setBounds(233, 242, 80, 27);
                _SubmitButton.setText("生成");

                _SubmitButton.addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        for (TableItem item : _Table.getItems())
                        {
                            if (item.getChecked())
                            {
                                selectExtMethod = item.getText(0);
                            }
                        }

                        String name = CommonUtil.generateEntityNameBySourceTable(currentSelectedTable);

                        List<FieldInfo> temps = new ArrayList<FieldInfo>(fieldMap.values());

                        // 是否存在IsExist字段
                        boolean isExist = false;
                        for (FieldInfo info : fieldMap.values())
                        {
                            if (info.getName().equalsIgnoreCase("isexist"))
                                isExist = true;
                        }
                        Map<String, Boolean> selectMethod = new HashMap<>();
                        if (!selectExtMethod.isEmpty())
                        {
                            String[] ss = selectExtMethod.split("\\---");
                            selectMethod.put(ss[0], isExist);
                        }

                        boolean isAddChangedMap = check1.getSelection();
                        boolean isCreateCache = check2.getSelection();
                        boolean isDBOperate = check3.getSelection();

                        UILogicController.generateUserTableCode(name, currentSelectedTable, fieldMap, temps,
                                selectMethod, isAddChangedMap, isCreateCache,isDBOperate);

                        codeDialog.dispose();
                    }
                });

                codeDialog.open();
            }
        });
        codeCreate.setText("输出Dao代码(t_u或者t_l)");
    }

    private static void ormUI(ToolBar codeBar, Shell shell, Display display)
    {
        if (MainWindow.ORMFLAG == 1)
        {
            ToolItem ormcodeCreate = new ToolItem(codeBar, SWT.NONE);
            ormcodeCreate.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent arg0)
                {
                    if (currentSelectedTable == null || table == null)
                        return;
                    /* 生成代码预览窗口, 获取焦点 */
                    codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                    Point pt = display.getCursorLocation();
                    codeDialog.setLocation(pt);
                    codeDialog.setLayout(new FillLayout(SWT.VERTICAL));
                    codeDialog.setText("提示：");

                    final Label _Label = new Label(codeDialog, SWT.BORDER);
                    _Label.setText("请选择需要拓展的方法");
                    _Table = new Table(codeDialog, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

                    TableItem _TableItem2 = new TableItem(_Table, SWT.NONE);
                    _TableItem2.setText(0, "CreateBeanAnnotation");
                    _TableItem2.setChecked(true);

                    final Composite _Composite = new Composite(codeDialog, SWT.BOTTOM);
                    _Composite.setLayout(new FillLayout());
                    final Button _SubmitButton = new Button(_Composite, SWT.BOTTOM);
                    _SubmitButton.setText("生成");

                    _SubmitButton.addSelectionListener(new SelectionAdapter()
                    {
                        @Override
                        public void widgetSelected(SelectionEvent arg0)
                        {
                            for (TableItem item : _Table.getItems())
                            {
                                if (item.getChecked())
                                {
                                    selectExtMethod = item.getText(0);
                                }
                            }

                            String name = CommonUtil.generateEntityNameBySourceTable(currentSelectedTable);

                            Map<String, Boolean> selectMethod = new HashMap<>();
                            if (!selectExtMethod.isEmpty())
                            {
                                String[] ss = selectExtMethod.split("\\---");
                                selectMethod.put(ss[0], true);
                            }
                            UILogicController.generateORMCode(name, currentSelectedTable, selectMethod);

                            codeDialog.dispose();
                        }
                    });
                    codeDialog.setSize(600, 500);
                    codeDialog.open();
                }
            });
            ormcodeCreate.setText("输出ORM代码（针对数据库）");
        }
    }

    private static void beanUI(ToolBar codeBar, Shell shell, Display display)
    {
        final ToolItem dropdown = new ToolItem(codeBar, SWT.DROP_DOWN);
        dropdown.setText("Bean代码");

        final Menu menu = new Menu(shell, SWT.POP_UP);
        MenuItem choice1 = new MenuItem(menu, SWT.PUSH);
        choice1.setText("单个Bean代码");
        choice1.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (selectType == 0 || currentSelectedTable == null || table == null
                        || currentSelectedTable.indexOf(".xls") >= 0)
                    return;

                /* 生成代码预览窗口, 获取焦点 */
                codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setLayout(new FillLayout(SWT.VERTICAL));
                codeDialog.setText("提示：");

                final Label _Label = new Label(codeDialog, SWT.BORDER);
                _Label.setText("请选择需要拓展的方法");
                _Table = new Table(codeDialog, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

                final Composite _Composite = new Composite(codeDialog, SWT.BOTTOM);
                _Composite.setLayout(new FillLayout());
                final Button _SubmitButton = new Button(_Composite, SWT.BOTTOM);
                _SubmitButton.setText("生成");

                _SubmitButton.addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        for (TableItem item : _Table.getItems())
                        {
                            if (item.getChecked())
                            {
                                selectExtMethod = item.getText(0);
                            }
                        }

                        String name = CommonUtil.generateEntityNameBySourceTable(currentSelectedTable);

                        UILogicController.generateSystemCode(name, currentSelectedTable);
                        codeDialog.dispose();
                    }
                });
                codeDialog.setSize(600, 500);
                codeDialog.open();
            }
        });

        MenuItem choice2 = new MenuItem(menu, SWT.PUSH);
        choice2.setText("Excel内所有Bean代码");
        choice2.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (selectType == 0 || currentSelectedTable == null || currentSelectedTable.indexOf(".xls") < 0)
                    return;

                /* 生成代码预览窗口, 获取焦点 */
                codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setLayout(new FillLayout(SWT.VERTICAL));
                codeDialog.setText("提示：");

                final Label _Label = new Label(codeDialog, SWT.BORDER);
                _Label.setText("请选择需要拓展的方法");
                _Table = new Table(codeDialog, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

                final Composite _Composite = new Composite(codeDialog, SWT.BOTTOM);
                _Composite.setLayout(new FillLayout());
                final Button _SubmitButton = new Button(_Composite, SWT.BOTTOM);
                _SubmitButton.setText("生成");

                _SubmitButton.addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        for (TableItem item : _Table.getItems())
                        {
                            if (item.getChecked())
                            {
                                selectExtMethod = item.getText(0);
                            }
                        }

                        AllSource source = (AllSource) dataSource;
                        Map<String, ExcelFile> map = source.excelSource.fileMap;
                        int count = 0;
                        // 生成所有excel的bean代码
                        for (Entry<String, ExcelFile> entry : map.entrySet())
                        {
                            if (entry.getKey().equalsIgnoreCase(currentSelectedTable))
                            {
                                List<ExcelSheet> list = entry.getValue().getSheetList();
                                for (ExcelSheet sheet : list)
                                {
                                    UILogicController.generateAllCode(sheet);
                                    count++;
                                }
                                break;
                            }
                        }
                        System.err.println("Auto Create Bean Count:" + count);

                        codeDialog.dispose();
                    }
                });
                codeDialog.setSize(600, 500);
                codeDialog.open();
            }
        });

        MenuItem choice3 = new MenuItem(menu, SWT.PUSH);
        choice3.setText("所有Excel的Bean代码");
        choice3.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                if (selectType == 0)
                    return;

                /* 生成代码预览窗口, 获取焦点 */
                codeDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
                Point pt = display.getCursorLocation();
                codeDialog.setLocation(pt);
                codeDialog.setLayout(new FillLayout(SWT.VERTICAL));
                codeDialog.setText("提示：");

                final Label _Label = new Label(codeDialog, SWT.BORDER);
                _Label.setText("请选择需要拓展的方法");
                _Table = new Table(codeDialog, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

                final Composite _Composite = new Composite(codeDialog, SWT.BOTTOM);
                _Composite.setLayout(new FillLayout());
                final Button _SubmitButton = new Button(_Composite, SWT.BOTTOM);
                _SubmitButton.setText("生成");

                _SubmitButton.addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent arg0)
                    {
                        for (TableItem item : _Table.getItems())
                        {
                            if (item.getChecked())
                            {
                                selectExtMethod = item.getText(0);
                            }
                        }

                        AllSource source = (AllSource) dataSource;
                        Map<String, ExcelFile> map = source.excelSource.fileMap;
                        int count = 0;
                        // 生成所有excel的bean代码
                        for (Entry<String, ExcelFile> entry : map.entrySet())
                        {
                            List<ExcelSheet> list = entry.getValue().getSheetList();
                            for (ExcelSheet sheet : list)
                            {
                                UILogicController.generateAllCode(sheet);
                                count++;
                            }
                        }
                        System.err.println("Auto Create Bean Count:" + count);

                        codeDialog.dispose();
                    }
                });
                codeDialog.setSize(600, 500);
                codeDialog.open();
            }
        });

        dropdown.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event event)
            {
                if (event.detail == SWT.ARROW)
                {
                    Rectangle rect = dropdown.getBounds();
                    Point pt = new Point(rect.x, rect.y + rect.height);
                    pt = codeBar.toDisplay(pt);
                    menu.setLocation(pt.x, pt.y);
                    menu.setVisible(true);
                }
            }
        });
    }

    private static void dataTableUI(Composite compContentShow)
    {
        Composite compTable = new Composite(compContentShow, SWT.NONE);
        compTable.setLayout(new FillLayout(SWT.HORIZONTAL));
        compTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        table = new Table(compTable, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (int i = 0; i < titles.length; i++)
        {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
        }

        table.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event e)
            {
                String clickItemName = ((TableItem) e.item).getText();
                System.err.println("选择：" + clickItemName);
                boolean tmp = ((TableItem) e.item).getChecked();
                ((TableItem) e.item).setChecked(tmp);
                fieldMap.get(clickItemName).setFlag(tmp);
            }
        });
    }

    private static GridLayout getGridLayout()
    {
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        return gridLayout;
    }
}
