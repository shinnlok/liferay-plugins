<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/macro.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/asset.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/class_names.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/company.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/default_dl_file_type.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/default_user.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/groups.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/roles.ftl">

<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/counters.ftl">

<#setting number_format = "computer">

<#list dataFactory.syncDLObjectModels as syncDLObjectModel>
	insert into SyncDLObject values (${syncDLObjectModel.syncDLObjectId}, ${syncDLObjectModel.companyId}, ${syncDLObjectModel.createTime}, ${syncDLObjectModel.modifiedTime}, ${syncDLObjectModel.repositoryId}, ${syncDLObjectModel.parentFolderId}, '${syncDLObjectModel.name}', '${syncDLObjectModel.extension}', '${syncDLObjectModel.mimeType}', '${syncDLObjectModel.description}', '${syncDLObjectModel.changeLog}', '${syncDLObjectModel.extraSettings}', '${syncDLObjectModel.version}', ${syncDLObjectModel.versionId}, ${syncDLObjectModel.size}, '${syncDLObjectModel.checksum}', '${syncDLObjectModel.event}', ${(syncDLObjectModel.lockExpirationDate)!'null'}, ${syncDLObjectModel.lockUserId}, '${syncDLObjectModel.lockUserName}', '${syncDLObjectModel.type}', ${syncDLObjectModel.typePK}, '${syncDLObjectModel.typeUuid}');
</#list>