package io.xdd.blackscience.socksserver.common;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器管理器,负责管理已经注册的服务器
 * */
public class ShadowSocksServerManager {

    private ArrayList<ShadowSocksServerInstance> address=new ArrayList<>();

    /**
     * 返回一个最佳的服务器地址
     * */
    public ShadowSocksServerInstance getOne(){
        return address.get(0);//永远返回第一个,其余的先不管了
    }

    private static ShadowSocksServerManager ourInstance = new ShadowSocksServerManager();

    public static ShadowSocksServerManager getInstance() {
        return ourInstance;
    }

    private ShadowSocksServerManager(){
        try {
            URL url = getClass().getClassLoader().getResource("address.cvs");
            Path path = null;
            if (url != null) {
                path = Paths.get(url.toURI());
                List<String> list = Files.readAllLines(path, Charset.defaultCharset());
                list.forEach(s -> {
                    ShadowSocksServerInstance instance=new ShadowSocksServerInstance();
                    String[] strs=s.split(",");
                    instance.setAddress(strs[0]);
                    instance.setPort(Integer.valueOf(strs[1]));
                    instance.setPassword(strs[2]);
                    address.add(instance);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ShadowSocksServerManager.getInstance();
    }
}
