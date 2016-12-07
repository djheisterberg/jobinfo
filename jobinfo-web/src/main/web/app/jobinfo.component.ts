import { Component, Input } from '@angular/core';

import { JobInfo } from './jobinfo';

@Component( {
    moduleId: module.id,
    selector: 'jobinfo',
    templateUrl: 'jobinfo.component.html'
})
export class JobInfoComponent {
    @Input() jobInfo: JobInfo;
}
