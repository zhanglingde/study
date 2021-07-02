package annotation;

/**
 * 测试所有类型注解的实体类
 */
public @TypeAnno(desc = "用户实体类") class User {

    @FieldAnno
    private String userId;

    @FieldAnno(desc = "用户名")
    private String username;

    @ConstructorAnno
    public User() {
    }

    @MethodAnno(time = "2021-07-01",sex = false)
    public void work(@ParamAnno(value = "1001") String userId, @ParamAnno(value = "张三")String username) {
        @LocalAnno(desc = "flag 局部变量")
        boolean flag;
    }
}
