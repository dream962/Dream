package com.deploy.ui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @version 
 *
 */
class MouseLisnterImp implements MouseListener
{
    Text text;
    Shell shell;
    DirectoryDialog directoryDialog;
    
    public MouseLisnterImp(Text text, Shell shell)
    {
        super();
        this.text = text;
        this.shell = shell;
        this.directoryDialog = new DirectoryDialog(shell);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e)
    {
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDown(MouseEvent e)
    {
        directoryDialog.setFilterPath(text.getText());
        String direString = directoryDialog.open();
        if (direString != null)
        {
            text.setText(direString);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseUp(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
}
