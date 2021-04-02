package ${packageName}.service;

import ${packageName}.model.${modelName};

@Service
public class ${serviceName}{

    @Autowired
    ${mapperName} ${mapperName?uncap_first};

    public List<${modelName}> getAll${modelName}s(){
        return ${mapperName?uncap_first}.getAll${modelName}s();
    }
}

