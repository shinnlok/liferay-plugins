<#setting number_format = "computer">

<#list dataFactory.syncDLObjectModels as syncDLObjectModel>
insert into SyncDLObject values (${syncDLObjectModel.objectId}, ${syncDLObjectModel.companyId}, ${syncDLObjectModel.createTime}, ${syncDLObjectModel.modifiedTime}, ${syncDLObjectModel.repositoryId}, ${syncDLObjectModel.parentFolderId}, '${syncDLObjectModel.name}', '${syncDLObjectModel.description}', '${syncDLObjectModel.checksum}', '${syncDLObjectModel.event}', ${syncDLObjectModel.lockUserId}, '${syncDLObjectModel.lockUserName}', ${syncDLObjectModel.size}, '${syncDLObjectModel.type}', ${syncDLObjectModel.typePK}, '${syncDLObjectModel.typeUuid}', '${syncDLObjectModel.version}');
</#list>