package annotation;

/**
 * 测试接口
 */
public interface UserImpl {

    public void work(@ParamAnno(value = "学生") String stuType,@ParamAnno(value = "大扫除") String things);
}
