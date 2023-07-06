package com.jnetdata.msp.media.service;

import java.io.IOException;
import java.sql.SQLException;

public interface FznService {
    String pagefznStyles(int type, int current, Integer size) throws IOException, SQLException;
}
