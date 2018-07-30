[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='command' /][/#assign]
[@ww.textfield labelKey="applitools.api.key.label" name="APPLITOOLS_API_KEY" required='true'/]

[#--[@ww.textfield labelKey="executable.label" name="command" required='true'/]--]

[@ww.select cssClass="builderSelectWidget" labelKey='executable.type' name='selectedExecutable'
list=uiConfigBean.getExecutableLabels('command') extraUtility=addExecutableLink /]

[@ww.textfield labelKey="executable.params.label" name="params" required='false'/]
[@ww.textarea labelKey="envvars.label" name="envvars" required='false' cols="30" rows="8"/]


