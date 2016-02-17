package io.xdd.blackscience.socksserver.local.manager;


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
public class SSServerManager {

    private ArrayList<SSServerInstance> address=new ArrayList<>();

    /**
     * 返回一个最佳的服务器地址
     * */
    public SSServerInstance getOne(){
        return address.get(0);//永远返回第一个,其余的先不管了
    }

    private static SSServerManager ourInstance = new SSServerManager();

    public static SSServerManager getInstance() {
        return ourInstance;
    }

    private SSServerManager(){

        //load 加载配置
        try {
            URL url = getClass().getClassLoader().getResource("address.cvs");
            Path path = null;
            if (url != null) {
                path = Paths.get(url.toURI());
                List<String> list = Files.readAllLines(path, Charset.defaultCharset());
                list.forEach(s -> {
                    SSServerInstance instance=new SSServerInstance();
                    String[] strs=s.split(",");
                    instance.setAddress(strs[0]);
                    instance.setPort(Integer.valueOf(strs[1]));
                    instance.setMethod(strs[2]);
                    instance.setPassword(strs[3]);
                    address.add(instance);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SSServerManager.getInstance();
    }
}
