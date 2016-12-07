import { Component } from '@angular/core';

import { Job } from './job';
import { JobInfo } from './jobinfo';
import { JobInfoService } from './jobinfo.service';

@Component( {
    moduleId: module.id,
    selector: 'the-app',
    templateUrl: 'app.component.html'
})
export class AppComponent {
    name = 'JobInfo';
    title = "Job Info";

    jobId: string;
    system: string;

    jobInfo: JobInfo;

    constructor( private jobInfoSvc: JobInfoService ) {
    }

    submit(): void {
        if ( this.jobId && this.system ) {
            this.jobInfoSvc.getJobInfo( this.jobId, this.system ).then( jobInfo => this.jobInfo = jobInfo );
        }
    }
}
