package com.upload.util;

import com.upload.data.ServerType;

public class ShellUtils
{
    public static final String GameServer_Shell = "gameserver.sh";

    public static final String GateServer_Shell = "gatewayserver.sh";

    public static final String AccountServer_Shell = "accountserver.sh";

    public static final String ChargeServer_Shell = "sdkserver.sh";

    public static String getShell(int gameType, int subType)
    {
        String shell = "";
        if (gameType == ServerType.GAME_SERVER)
        {
            if (subType == 1)
                shell = GameServer_Shell;
            else
                shell = GateServer_Shell;
        }
        if (gameType == ServerType.ACCOUNT_SERVER)
        {
            shell = AccountServer_Shell;
        }
        if (gameType == ServerType.CHARGE_SERVER)
        {
            shell = ChargeServer_Shell;
        }
        return shell;
    }
}
