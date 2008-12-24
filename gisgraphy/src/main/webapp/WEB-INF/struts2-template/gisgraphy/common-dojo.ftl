<#macro handleDefaultOverrides overrides>
	var ${overrides} = {
		required: ${parameters.required?default(false)?string},
		name: dojo.byId("${parameters.id?html}").name
	};
	<#if parameters.cssStyle?exists>
		${overrides}.style = '${parameters.cssStyle}';
	</#if>
	<#if parameters.cssClass?exists>
		${overrides}.class = '${parameters.cssClass}';
	</#if>
	
	<@handleOnChange overrides=overrides />
	<@handleConstraints overrides=overrides />
	${overrides}.intermediateChanges = true;
				
</#macro>

<#macro handleOnChange overrides>
	<#if parameters.onchange?exists>
		${overrides}.onChange = function(value) {
			eval("${parameters.onchange}");
		}
	</#if>
</#macro>

<#macro handleConstraints overrides>
	var constraints = {};
	<#if parameters.dojoMinConstraint?exists>
			constraints.min = ${parameters.dojoMinConstraint};
	</#if>
	<#if parameters.dojoMaxConstraint?exists>
			constraints.max = ${parameters.dojoMaxConstraint};
	</#if>
	<#if parameters.dojoPatternConstraint?exists>
			constraints.pattern = '${parameters.dojoPatternConstraint}';
	</#if>
	${overrides}.constraints = constraints;
</#macro>