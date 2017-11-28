import { ICreateDatasetRequest } from "@palantir/product";
import { IBackingFileSystem } from "@palantir/product-datasets";
import { IDataset } from "@palantir/product-datasets";

export interface ITestService {
    createDataset(
        request: ICreateDatasetRequest,
        testHeaderArg: string
    ): Promise<IDataset>;

    getBranches(
        datasetRid: string
    ): Promise<string[]>;

    /** Gets all branches of this dataset. */
    getBranchesDeprecated(
        datasetRid: string
    ): Promise<string[]>;

    getDataset(
        datasetRid: string
    ): Promise<IDataset | null | undefined>;

    /** Returns a mapping from file system id to backing file system configuration. */
    getFileSystems(): Promise<{ [key: string]: IBackingFileSystem }>;

    getRawData(
        datasetRid: string
    ): Promise<any>;

    maybeGetRawData(
        datasetRid: string
    ): Promise<any | null | undefined>;

    resolveBranch(
        datasetRid: string,
        branch: string
    ): Promise<string | null | undefined>;

    testBoolean(): Promise<boolean>;

    testDouble(): Promise<number>;

    testInteger(): Promise<number>;

    testParam(
        datasetRid: string
    ): Promise<string | null | undefined>;

    testQueryParams(
        something: string
    ): Promise<number>;

    uploadRawData(
        input: any
    ): Promise<void>;
}