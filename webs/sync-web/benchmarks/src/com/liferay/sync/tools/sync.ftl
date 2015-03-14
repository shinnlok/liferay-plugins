<#include "/com/liferay/portal/tools/samplesqlbuilder/dependencies/sample.ftl">

<#setting number_format = "computer">

<#list dataFactory.syncDLObjectModels as syncDLObjectModel>
insert into SyncDLObject values (${syncDLObjectModel.syncDLObjectId}, ${syncDLObjectModel.companyId}, ${syncDLObjectModel.createTime}, ${syncDLObjectModel.modifiedTime}, ${syncDLObjectModel.repositoryId}, ${syncDLObjectModel.parentFolderId}, '${syncDLObjectModel.name}', '${syncDLObjectModel.description}', '${syncDLObjectModel.checksum}', '${syncDLObjectModel.event}', ${syncDLObjectModel.lockUserId}, '${syncDLObjectModel.lockUserName}', ${syncDLObjectModel.size}, '${syncDLObjectModel.type}', ${syncDLObjectModel.typePK}, '${syncDLObjectModel.typeUuid}', '${syncDLObjectModel.version}', '${syncDLObjectModel.versionId}');
</#list>