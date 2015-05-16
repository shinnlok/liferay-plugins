<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/macro.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/asset.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/class_names.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/company.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/default_dl_file_type.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/default_user.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/groups.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/roles.ftl">

<#include "/com/liferay/portal/tools/sample/sql/builder/dependencies/counters.ftl">

<#setting number_format = "computer">

insert into Release_ (releaseId, createDate, modifiedDate, servletContextName, buildNumber, verified) values (2, now(), now(), 'sync-web', 100, 0);

<#list dataFactory.syncDLObjectModels as syncDLObjectModel>
	insert into SyncDLObject values (${syncDLObjectModel.syncDLObjectId}, ${syncDLObjectModel.companyId}, ${syncDLObjectModel.createTime}, ${syncDLObjectModel.modifiedTime}, ${syncDLObjectModel.repositoryId}, ${syncDLObjectModel.parentFolderId}, '${syncDLObjectModel.name}', '${syncDLObjectModel.extension}', '${syncDLObjectModel.mimeType}', '${syncDLObjectModel.description}', '${syncDLObjectModel.changeLog}', '${syncDLObjectModel.extraSettings}', '${syncDLObjectModel.version}', ${syncDLObjectModel.versionId}, ${syncDLObjectModel.size}, '${syncDLObjectModel.checksum}', '${syncDLObjectModel.event}', ${(syncDLObjectModel.lockExpirationDate)!'null'}, ${syncDLObjectModel.lockUserId}, '${syncDLObjectModel.lockUserName}', '${syncDLObjectModel.type}', ${syncDLObjectModel.typePK}, '${syncDLObjectModel.typeUuid}');
</#list>