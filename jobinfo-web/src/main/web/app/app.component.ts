import { Component } from '@angular/core';
import { Response } from '@angular/http';

import { Job } from './job';
import { JobInfo } from './jobinfo';
import { JobInfoService } from './jobinfo.service';

@Component( {
    moduleId: module.id,
    selector: 'the-app',
    templateUrl: 'app.component.html'
})
export class AppComponent {
    
    needsAuthentication = true;
    username: string;
    password: string;
    name = 'JobInfo';
    title = "Job Info";

    jobId: string;
    system: string;

    jobInfo: JobInfo;

    constructor( private jobInfoSvc: JobInfoService ) {
    }

    submit(): void {
        let self = this;
        this.needsAuthentication = false;
        if ( this.jobId && this.system ) {
            this.jobInfoSvc.getJobInfo( this.username, this.password, this.jobId, this.system ).then( jobInfo => this.jobInfo = jobInfo ).catch(function(response) {
                self.needsAuthentication = true;
                alert("response is '" + response + "'");
            });
        }
    }
    
    handleError(response: Response | any) {
        this.needsAuthentication = true;
        alert("response is '" + response + "'");
    }
}
