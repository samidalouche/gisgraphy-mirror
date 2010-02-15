<#-- OUT OF DATE. REFER TO hidden.ftl -->

<#include "/${parameters.templateDir}/${parameters.theme}/common-dojo.ftl" />
<#-- Integration is far from being complete..-->
<script type="text/javascript">
	dojo.require("dijit.form.DateTextBox");
	dojo.require("dojo.date.stamp");
	dojo.addOnLoad(function() {
		var value = dojo.byId("${parameters.id?html}").value;
		<@handleDefaultOverrides overrides="overrides"/>
		<#--
		${localizationHelper.getBestDateTimePattern()}
		dd/MM/yyyy
		overrides.constraints.datePattern = '${localizationHelper.getBestDateTimePattern()}';
		-->
		
		new dijit.form.DateTextBox(overrides, dojo.byId("${parameters.id?html}"));
		if(value != null && value.length > 0) {
			var dateValue = dojo.date.stamp.fromISOString(value);
			dijit.byId("${parameters.id?html}").setValue(dateValue);
		}
	});
</script>
<#include "/${parameters.templateDir}/${parameters.theme}/controlheader.ftl" />
<#include "/${parameters.templateDir}/simple/text.ftl" />
<#include "/${parameters.templateDir}/${parameters.theme}/controlfooter.ftl" />
